package net.rdrei.android.wakimail.ui;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.data.MailDatabase;
import net.rdrei.android.wakimail.data.MailPreferences;
import net.rdrei.android.wakimail.data.MailTable;
import net.rdrei.android.wakimail.task.MailSyncTask;
import roboguice.activity.RoboListActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MailListActivity extends RoboListActivity {

	private final class OnRefreshClickListener implements View.OnClickListener {
		public void onClick(View v) {
			refresh();
		}
	}

	private static final int ABOUT_DIALOG = 0;

	private static final String[] PROJECTION = { MailTable.Columns._ID,
			MailTable.Columns.TITLE, MailTable.Columns.SENDER, };

	public static final String USER_EXTRA = "user";

	private SimpleCursorAdapter adapter;

	private Cursor listCursor;
	
	private RoboAsyncTask<?> mSyncTask;

	@InjectView(R.id.mail_loadingspinner)
	private ProgressBar mLoadingSpinner;
	
	@InjectView(R.id.refresh_btn)
	private Button mRefreshButton;

	private void bindRefreshButton() {
		this.mRefreshButton.setOnClickListener(new OnRefreshClickListener());
	}

	/**
	 * Delete all user information from the storage and end
	 */
	private void logoutUser() {
		// Clear the preferences.
		final SharedPreferences preferences = getSharedPreferences(
				MailPreferences.KEY, MODE_PRIVATE);
		preferences.edit().clear().commit();
		// Drop the database.
		if (!this.deleteDatabase(MailDatabase.DB_NAME)) {
			Ln.e("Could not delete sqlite database!");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_list);
		this.bindRefreshButton();

		// This is an action on the main thread and is practically unlimited.
		// I'm not sure for how many mails this will work. It should
		// definitely better be paginated and loaded on a different thread.
		// There's actually a ListLoader-something adapter in Android >= 2.3
		// which should be used.

		// Set up the mail display using a SimpleCursorAdapter.
		final Uri uri = MailTable.ALL_MAILS_URI;
		this.listCursor = managedQuery(uri, PROJECTION, null, null, null);
		this.adapter = new SimpleCursorAdapter(this,
				android.R.layout.two_line_list_item, this.listCursor,
				new String[] { MailTable.Columns.TITLE,
						MailTable.Columns.SENDER }, new int[] {
						android.R.id.text1, android.R.id.text2 }, 0);
		setListAdapter(this.adapter);
		
		// If a previous sync is still running.
		if (this.mSyncTask != null) {
			this.showLoadingSpinner();
		}

		if (this.adapter.isEmpty()) {
			Ln.d("List is empty. Triggering initial loading.");
			refresh();
		}
		
		bindListViewOnClick();
	}

	private void bindListViewOnClick() {
		final ListView listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// When clicked, open the detail view.
				Uri uri = ContentUris.withAppendedId(MailTable.ALL_MAILS_URI,
						id);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				MailListActivity.this.startActivity(intent);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case ABOUT_DIALOG:
			View view = LayoutInflater.from(this).inflate(
					R.layout.dialog_about, null);
			String title = this.getString(R.string.about_title);
			String positiveMessage = this.getString(android.R.string.ok);
			return new AlertDialog.Builder(this).setTitle(title)
					.setCancelable(true).setIcon(R.drawable.icon)
					.setPositiveButton(positiveMessage, null).setView(view)
					.create();
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (mSyncTask != null) {
			Ln.d("Canceling old sync task.");
			mSyncTask.cancel(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mail_list_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Implements the action handlers for when an item in the options
	 * menu is selected.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			this.showDialog(ABOUT_DIALOG);
			return true;
		case R.id.menu_logout:
			this.logoutUser();
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void refresh() {
		MailSyncTask task = new MailSyncTask(this) {
			@Override
			protected void onSuccess(Integer result) throws Exception {
				if (result > 0) {
					refreshListCursor();
				}
				super.onSuccess(result);
			}

			@Override
			protected void onFinally() throws RuntimeException {
				hideLoadingSpinner();
				mSyncTask = null;
				super.onFinally();
			}
		};

		Ln.d("Starting mail sync task.");
		synchronized (this) {
			if (this.mSyncTask == null) {
				mSyncTask = task;
				task.execute();
				showLoadingSpinner();
			} else {
				Ln.w("Refusing to start new sync task while old is still " +
					 "running!");
			}
		}
	}

	private void refreshListCursor() {
		Ln.d("Refreshing list.");
		// XXX: Once again, this is a synchronous operation on the UI thread
		// and should be moved to a separate async query that reloads
		// afterwards.
		this.listCursor.requery();
		this.adapter.notifyDataSetChanged();
	}

	private void showLoadingSpinner() {
		this.mRefreshButton.setVisibility(View.GONE);
		this.mLoadingSpinner.setVisibility(View.VISIBLE);
	}
	
	private void hideLoadingSpinner() {
		this.mLoadingSpinner.setVisibility(View.GONE);
		this.mRefreshButton.setVisibility(View.VISIBLE);
	}
}
