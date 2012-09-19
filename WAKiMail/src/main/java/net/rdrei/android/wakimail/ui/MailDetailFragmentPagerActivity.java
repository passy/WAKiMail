package net.rdrei.android.wakimail.ui;

import java.util.ArrayList;
import java.util.List;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.data.MailTable;
import net.rdrei.android.wakimail.ui.MailDetailFragment.GetTitleCallback;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MailDetailFragmentPagerActivity extends
		RoboFragmentActivity implements LoaderCallbacks<Cursor> {

	private static final String[] PROJECTION = { MailTable.Columns._ID };

	@InjectView(R.id.pager)
	ViewPager mPager;

	@InjectView(R.id.position)
	TextView mPositionText;

	@InjectView(R.id.newer)
	View mNewerText;

	@InjectView(R.id.older)
	View mOlderText;

	@InjectView(R.id.position_bar)
	View mPositionBar;

	/**
	 * Stores the total count of mails. Is set asynchronously. Being -1
	 * indicates that it's not loaded yet.
	 */
	private int mItemCount = -1;

	/**
	 * Stores the URI this Activity was called with initially.
	 */
	private Uri mUri;

	private MailDetailFragmentPagerAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_detail_fragment_pager);

		mUri = this.getIntent().getData();

		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				MailDetailFragmentPagerActivity.this.onPageSelected(position);
			}
		});
		getSupportLoaderManager().initLoader(0, null, this);

		final ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
	}

	protected void onPageSelected(int position) {
		mPositionText.setText(getString(R.string.pager_position, position + 1,
				mItemCount));

		if (position == 0) {
			mNewerText.setVisibility(View.INVISIBLE);
		} else {
			mNewerText.setVisibility(View.VISIBLE);
		}

		if (position == mItemCount - 1) {
			mOlderText.setVisibility(View.INVISIBLE);
		} else {
			mOlderText.setVisibility(View.VISIBLE);
		}

		MailDetailFragment fragment = null;
		try {
			fragment = (MailDetailFragment) mAdapter.getFragment(position);
		} catch (IndexOutOfBoundsException e) {
			Ln.w("GC cleaned up our active fragment. Weird.");
		}

		if (fragment != null) {
			fragment.getTitleAsync(new GetTitleCallback() {
				@Override
				public void onGetTitle(CharSequence title) {
					Ln.d("Setting title from fragment pager: " + title);
					setTitle(title);
				}
			});
		} else {
			Ln.w("Fragment wasn't cached. Can't set title.");
			setTitle(null);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(this, MailTable.ALL_MAILS_URI, PROJECTION,
				null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Ln.d("Mail list finished.");

		// Store the total count which is displayed
		mItemCount = cursor.getCount();
		// ... and used as initial capacity.
		final ArrayList<Integer> mailIdMap = new ArrayList<Integer>(mItemCount);

		try {
			// Save the IDs, so we can map position -> id
			while (cursor.moveToNext()) {
				mailIdMap.add(cursor.getInt(0));
			}
		} finally {
			cursor.close();
		}

		mAdapter = new MailDetailFragmentPagerAdapter(
				getSupportFragmentManager(), mailIdMap);

		// Set the new adapter.
		mPager.setAdapter(mAdapter);
		mPositionBar.setVisibility(View.VISIBLE);

		selectInitialPage(mailIdMap);
	}

	/**
	 * Select the page based on the URI this activity received.
	 */
	private void selectInitialPage(List<Integer> mailIdMap) {
		final int id = Integer.parseInt(mUri.getLastPathSegment());

		// Find the ID in our list.
		final int position = mailIdMap.indexOf(id);
		mPager.setCurrentItem(position);
		onPageSelected(position);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			// The app icon was clicked, move up to list.
			final Intent intent = new Intent(this, MailListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
