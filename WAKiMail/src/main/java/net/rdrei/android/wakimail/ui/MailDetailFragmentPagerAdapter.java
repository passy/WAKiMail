package net.rdrei.android.wakimail.ui;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rdrei.android.wakimail.data.MailTable;
import android.content.ContentUris;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

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

	/**
	 * Keeps a weak list to all fragments created.
	 */
	private Map<Integer, WeakReference<Fragment>> mFragments = new HashMap<Integer, WeakReference<Fragment>>();

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);

		mFragments.remove(position);
	}

	public MailDetailFragmentPagerAdapter(FragmentManager fm,
			List<Integer> mailIdMap) {
		super(fm);

		mMailIdMap = mailIdMap;
	}

	@Override
	public Fragment getItem(int position) {
		final int id = mMailIdMap.get(position);
		final Uri uri = ContentUris.withAppendedId(MailTable.ALL_MAILS_URI, id);

		Fragment fragment = MailDetailFragment.newInstance(uri);
		mFragments.put(position, new WeakReference<Fragment>(fragment));

		return fragment;
	}

	/**
	 * Tries to return a reference to the fragment at the given position.
	 * 
	 * @param index
	 * @return Either a fragment or null.
	 */
	public Fragment getFragment(int position) {
		WeakReference<Fragment> weakReference = mFragments.get(position);
		return weakReference.get();
	}

	@Override
	public int getCount() {
		return mMailIdMap.size();
	}

}
