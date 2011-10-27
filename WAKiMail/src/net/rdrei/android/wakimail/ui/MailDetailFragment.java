package net.rdrei.android.wakimail.ui;

import net.rdrei.android.wakimail.R;
import roboguice.fragment.RoboListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;

public class MailDetailFragment extends RoboListFragment {

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

	/**resId
	 * When the fragment is loaded, rendered and embedded into the activity.
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Set the placeholder text when the list is empty.
		this.setEmptyText(this.getText(R.string.loading));
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
}
