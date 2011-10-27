package net.rdrei.android.wakimail.ui;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.data.MailTable;
import roboguice.fragment.RoboListFragment;
import roboguice.util.Ln;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.View;
import android.widget.TextView;

public class MailDetailFragment extends RoboListFragment implements
		LoaderCallbacks<Cursor>, ViewBinder {

	public final static String KEY_URI = "mailDetailURI";

	/**
	 * This is the Adapter being used to display the list's data.
	 */
	private SimpleCursorAdapter mAdapter;
	private Uri mUri;

	public static MailDetailFragment newInstance(Uri uri) {
		MailDetailFragment fragment = new MailDetailFragment();
		Bundle args = new Bundle();
		args.putString(MailDetailFragment.KEY_URI, uri.toString());
		fragment.setArguments(args);

		return fragment;
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

		this.mUri = Uri.parse(uri);
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

		// Set the placeholder text when the list is empty.
		setEmptyText(this.getText(R.string.loading));

		mAdapter = new SimpleCursorAdapter(
		// The activity the fragment is using.
				getActivity(),
				// The layout built to display the mail.
				R.layout.mail_details,
				// The cursor is loaded asynchronously and not available yet.
				null,
				// The map projection received.
				new String[] { MailTable.Columns.TITLE,
						MailTable.Columns.SENDER, MailTable.Columns.DATE},
				// The map to display to.
				new int[] { R.id.mail_title, R.id.mail_from, R.id.mail_date},
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
		outState.putString(KEY_URI, this.mUri.toString());
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// No need to filter or select anything specific, the URL fetches only
		// one entry.
		return new CursorLoader(getActivity(), mUri,
				MailTable.MAILS_PROJECTION, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
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
		case R.id.mail_body:
			if (cursor.isNull(columnIndex)) {
				Ln.i("Body isn't available yet. Sry.");
			} else {
				((TextView)view).setText(cursor.getString(columnIndex));
			}
			break;
		}
		return false;
	}
}
