package net.rdrei.android.wakimail.ui;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.data.MailTable;
import net.rdrei.android.wakimail.task.MailSyncTask;
import roboguice.fragment.RoboListFragment;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MailListFragment extends RoboListFragment implements
		LoaderCallbacks<Cursor> {

	private final class OnRefreshClickListener implements View.OnClickListener {
		public void onClick(View v) {
			refresh();
		}
	}

	private static final String[] PROJECTION = { MailTable.Columns._ID,
			MailTable.Columns.TITLE, MailTable.Columns.SENDER, };

	public static final String USER_EXTRA = "user";

	private SimpleCursorAdapter adapter;

	private RoboAsyncTask<?> mSyncTask;

	@InjectView(R.id.mail_loadingspinner)
	private ProgressBar mLoadingSpinner;

	@InjectView(R.id.refresh_btn)
	private Button mRefreshButton;

	private void bindRefreshButton() {
		this.mRefreshButton.setOnClickListener(new OnRefreshClickListener());
	}

	public interface OnLogoutRequestedListener {
		public void onLogoutRequested();
	}

	private OnLogoutRequestedListener mLogoutListener;

	/**
	 * Requests the logout listener from the activity we're attached to.
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		try {
			this.mLogoutListener = (OnLogoutRequestedListener) activity;
		} catch (ClassCastException e) {
			Ln.e(e);
			throw new ClassCastException(activity.toString()
					+ " must implement OnLogoutRequestedListener "
					+ "to use MailListFragment.");
		}

		super.onAttach(activity);
	}

	/**
	 * Inflates the layout used for this fragment from XML.
	 * 
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mail_list, container, false);
	}

	/**
	 * Bind views from the fragment.
	 * 
	 * @see roboguice.fragment.RoboListFragment#onViewCreated(android.view.View,
	 *      android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		bindRefreshButton();
		bindListViewOnClick();

		// Trigger initial loading.
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up the mail display using a SimpleCursorAdapter.
		this.adapter = new SimpleCursorAdapter(this.getActivity(),
				android.R.layout.two_line_list_item, null, new String[] {
						MailTable.Columns.TITLE, MailTable.Columns.SENDER },
				new int[] { android.R.id.text1, android.R.id.text2 }, 0);
		setListAdapter(this.adapter);
		setHasOptionsMenu(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu,
	 * android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.mail_list_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
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
				MailListFragment.this.startActivity(intent);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mSyncTask != null) {
			Ln.d("Canceling old sync task.");
			mSyncTask.cancel(true);
		}
	}

	/**
	 * Implements the action handlers for when an item in the options menu is
	 * selected.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			AboutDialogFragment dialog = AboutDialogFragment.newInstance();
			dialog.show(getFragmentManager(), "dialog");
			return true;
		case R.id.menu_logout:
			this.mLogoutListener.onLogoutRequested();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void refresh() {
		MailSyncTask task = new MailSyncTask(this.getActivity()) {
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
				Ln.w("Refusing to start new sync task while old is still "
						+ "running!");
			}
		}
	}

	private void showLoadingSpinner() {
		this.mRefreshButton.setVisibility(View.GONE);
		this.mLoadingSpinner.setVisibility(View.VISIBLE);
	}

	private void hideLoadingSpinner() {
		this.mLoadingSpinner.setVisibility(View.GONE);
		this.mRefreshButton.setVisibility(View.VISIBLE);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		showLoadingSpinner();
		return new CursorLoader(this.getActivity(), MailTable.ALL_MAILS_URI,
				PROJECTION, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		this.adapter.swapCursor(cursor);
		hideLoadingSpinner();

		if (this.adapter.isEmpty()) {
			Ln.d("List is empty. Triggering initial loading.");
			refresh();
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		this.adapter.swapCursor(null);
	}
}
