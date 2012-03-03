package net.rdrei.android.wakimail.ui;

import com.actionbarsherlock.app.ActionBar;

import net.rdrei.android.wakimail.R;
import roboguice.activity.RoboFragmentActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class MailDetailActivity extends RoboFragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Uri uri = this.getIntent().getData();
		
		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);
		
		fixActionBar(actionBar);

		setContentView(R.layout.activity_singlepane_empty);

		// First-time init; create fragment to embed in activity.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		MailDetailFragment newFragment = MailDetailFragment.newInstance(uri);
		ft.replace(R.id.root_container, newFragment);
		ft.commit();
		
	}

	/**
	 * Workaround for ABS, which does not like subtitles being set to early in
	 * the fragment lifecycle. This way, we can replace it whenever we want.
	 * This should not be faster than loading from the database (I hope).
	 */
	private void fixActionBar(ActionBar actionBar) {
		// By setting the mode to non-standard, we can later switch back to
		// cause a reflow which correctly displays the subtitle.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setSubtitle(R.string.loading);
	}
}
