package net.rdrei.android.wakimail.ui;

import net.rdrei.android.wakimail.R;
import roboguice.fragment.RoboListFragment;
import roboguice.inject.InjectResource;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

public class MailListFragment extends RoboListFragment implements
		LoaderCallbacks<Cursor> {
	
	// Resources
	@InjectResource(R.string.mail_list_empty) static private String 
		MAIL_LIST_EMPTY_TEXT;
	
	private SimpleCursorAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		setEmptyText(MAIL_LIST_EMPTY_TEXT);
		
		adapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_2, null,
				new String[] { "test", "test" },
				new int[] { 1, 2 }, 0);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}

}
