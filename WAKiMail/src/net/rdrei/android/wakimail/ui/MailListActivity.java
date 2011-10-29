package net.rdrei.android.wakimail.ui;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.data.MailTable;
import net.rdrei.android.wakimail.task.MailSyncTask;
import roboguice.activity.RoboListActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MailListActivity extends RoboListActivity {

	private class MailSyncTaskHandlerCallback implements Handler.Callback {

		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == MailSyncTask.SYNC_SUCCESS_MESSAGE) {
				Ln.d("UI thread received sync success message.");
				refreshList();
				hideLoadingSpinner();
			} else if (msg.what == MailSyncTask.SYNC_ERROR_MESSAGE) {
				Ln.w("MailSyncTask threw an exception.");
				hideLoadingSpinner();
			}
			return true;
		}

	}

	private final class OnRefreshClickListener implements View.OnClickListener {
		public void onClick(View v) {
			// TODO: Add loading spinner instead of button.
			Handler handler = new Handler(new MailSyncTaskHandlerCallback());
			MailSyncTask task = new MailSyncTask(MailListActivity.this, handler);

			Ln.d("Starting mail sync task.");
			task.execute();
			showLoadingSpinner();
		}
	}

	private static final String[] PROJECTION = { MailTable.Columns._ID,
			MailTable.Columns.TITLE, MailTable.Columns.SENDER, };
	public static final String USER_EXTRA = "user";
	private SimpleCursorAdapter adapter;
	private Cursor listCursor;

	@InjectView(R.id.refresh_btn)
	private Button mRefreshButton;
	
	@InjectView(R.id.mail_loadingspinner)
	private ProgressBar mLoadingSpinner;

	private void bindRefreshButton() {
		mRefreshButton.setOnClickListener(new OnRefreshClickListener());
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
	
	private void showLoadingSpinner() {
		mRefreshButton.setVisibility(View.GONE);
		mLoadingSpinner.setVisibility(View.VISIBLE);
	}
	
	private void hideLoadingSpinner() {
		mLoadingSpinner.setVisibility(View.GONE);
		mRefreshButton.setVisibility(View.VISIBLE);
	}

	private void refreshList() {
		Ln.d("Refreshing list.");
		// XXX: Once again, this is a synchronous operation on the UI thread
		// and should be moved to a separate async query that reloads
		// afterwards.
		this.listCursor.requery();
		this.adapter.notifyDataSetChanged();
	}
}
