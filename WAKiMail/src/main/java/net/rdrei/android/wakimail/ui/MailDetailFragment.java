package net.rdrei.android.wakimail.ui;

import java.text.SimpleDateFormat;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.data.MailTable;
import net.rdrei.android.wakimail.task.MailLoadTask;
import roboguice.fragment.RoboListFragment;
import roboguice.util.Ln;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MailDetailFragment extends RoboListFragment implements
		LoaderCallbacks<Cursor>, ViewBinder {

	public static final String KEY_URI = "mailDetailURI";

	/**
	 * This is the Adapter being used to display the list's data.
	 */
	private SimpleCursorAdapter mAdapter;
	private Uri mUri;
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
			"dd.MM.yyyy HH:mm");

	public static MailDetailFragment newInstance(Uri uri) {
		MailDetailFragment fragment = new MailDetailFragment();
		Bundle args = new Bundle();
		args.putString(MailDetailFragment.KEY_URI, uri.toString());
		fragment.setArguments(args);

		return fragment;
	}

	/**
	 * Load our custom layout instead of the generic list view.
	 * 
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.fragment_mail_detail, container, false);
	}

	/**
	 * Load either the old state or the arguments required to load the fragment,
	 * namely the URI parameter.
	 * 
	 * @see roboguice.fragment.RoboListFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle args = this.getArguments();
		String uri = args.getString(KEY_URI);

		// Maybe we can get the URI from the saved state?
		if (uri == null) {
			uri = savedInstanceState.getString(KEY_URI);
		}

		mUri = Uri.parse(uri);
	}

	/**
	 * resId When the fragment is loaded, rendered and embedded into the
	 * activity.
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mAdapter = new SimpleCursorAdapter(
		// The activity the fragment is using.
				getActivity(),
				// The layout built to display the mail.
				R.layout.mail_details,
				// The cursor is loaded asynchronously and not available yet.
				null,
				// The map projection received.
				new String[] { MailTable.Columns.BODY, MailTable.Columns.BODY,
						MailTable.Columns.SENDER, MailTable.Columns.DATE,
						MailTable.Columns.TITLE },
				// The map to display to.
				new int[] { R.id.mail_body, R.id.mail_loadingspinner,
						R.id.mail_from, R.id.mail_date, R.id.mail_title },
				// No flags.
				0);

		mAdapter.setViewBinder(this);
		setListAdapter(mAdapter);
		// Poke the loader to retrieve an async cursor.
		getLoaderManager().initLoader(0, null, this);
	}

	/**
	 * Save the current URL to the outState.
	 * 
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the URL in the out state for resuming.
		outState.putString(KEY_URI, mUri.toString());
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// No need to filter or select anything specific, the URL fetches only
		// one entry.
		Ln.d("Requesting a new loader for " + mUri);
		return new CursorLoader(getActivity(), mUri,
				MailTable.MAILS_PROJECTION, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Ln.d("Swapping cursor result.");
		// Make the new cursor the used cursor.
		mAdapter.swapCursor(cursor);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		int viewId = view.getId();

		switch (viewId) {
		case R.id.mail_title:
			final String text = cursor.getString(columnIndex);
			Ln.i("Setting new title: " + text);

			final TextView titleView = (TextView) view;

			titleView.setText(text);

			// Delaying the title update until the event loop is finished causes
			// the title to be displayed completely instead of being truncated
			// after the length of the previously displayed string is exceeded.
			getActivity().getWindow().getDecorView().post(new Runnable() {

				@Override
				public void run() {
					final FragmentActivity activity = getActivity();
					activity.setTitle(text);
				}
			});
			return true;
		case R.id.mail_date:
			Ln.d("Setting date.");
			long date = cursor.getLong(columnIndex);
			TextView dateView = (TextView) view;
			dateView.setText(DATE_FORMATTER.format(date));
			return true;
		case R.id.mail_body:
			if (cursor.isNull(columnIndex)) {
				loadMailBody();
			} else {
				view.setVisibility(View.VISIBLE);
				final String body = cursor.getString(columnIndex);
				((TextView) view).setText(this.formatMailBody(body));
			}
			return true;
		case R.id.mail_loadingspinner:
			if (cursor.isNull(columnIndex)) {
				view.setVisibility(View.VISIBLE);
			} else {
				view.setVisibility(View.GONE);
			}
			return true;
		}
		return false;
	}

	/**
	 * Removes unnecessary HTML elements from the mail body.
	 * 
	 * @param text
	 * @return Cleaned text.
	 */
	private CharSequence formatMailBody(String text) {
		text = text.replaceAll("<br>", "").replaceAll("<br/>", "")
				.replaceAll("<br />", "");
		return text;
	}

	/**
	 * Start the task downloading the mail body.
	 */
	private void loadMailBody() {
		Ln.d("Downloading mail body for URI " + mUri);
		Handler handler = new Handler(new MailLoadTaskHandlerCallback());
		MailLoadTask task = new MailLoadTask(getActivity(), handler, mUri);
		task.execute();
	}

	private class MailLoadTaskHandlerCallback implements Handler.Callback {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case MailLoadTask.LOAD_SUCCESS_MESSAGE:
				Ln.d("Mail body was loaded.");
				// mAdapter.notifyDataSetChanged();
				return true;
			case MailLoadTask.LOAD_ERROR_MESSAGE:
				// Stop the current activity. Actually this should be handled
				// by the activity rather than the fragment, but we will just
				// do it this way.
				final Activity activity = MailDetailFragment.this.getActivity();
				// The activity might have already been closed or be in a
				// transitional state.
				if (activity != null) {
					activity.finish();
				}
				return true;
			}
			return false;
		}

	}
}
