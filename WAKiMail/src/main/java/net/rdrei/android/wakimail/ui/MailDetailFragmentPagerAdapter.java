package net.rdrei.android.wakimail.ui;

import java.util.ArrayList;
import java.util.List;

import net.rdrei.android.wakimail.data.MailTable;
import android.content.ContentUris;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * The adapter used in the mail detail view providing the fragments in the
 * pager. Note that a Fragment<b>State</b>PagerAdapter is used to preserve
 * memory as the list of items may grow quite significantly over time.
 * 
 * @author pascal
 * 
 */
public class MailDetailFragmentPagerAdapter extends FragmentStatePagerAdapter {

	final private List<Integer> mMailIdMap;

	public MailDetailFragmentPagerAdapter(FragmentManager fm,
			List<Integer> mailIdMap) {
		super(fm);
		
		mMailIdMap = mailIdMap;
	}

	@Override
	public Fragment getItem(int position) {
		final int id = mMailIdMap.get(position);
		final Uri uri = ContentUris.withAppendedId(MailTable.ALL_MAILS_URI, id);

		return MailDetailFragment.newInstance(uri);
	}

	@Override
	public int getCount() {
		return 10;
	}

}
