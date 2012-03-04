package net.rdrei.android.wakimail.ui;

import net.rdrei.android.wakimail.R;
import roboguice.activity.RoboFragmentActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;

public class MailDetailActivity extends RoboFragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Uri uri = this.getIntent().getData();

		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

		setContentView(R.layout.activity_singlepane_empty);

		// First-time init; create fragment to embed in activity.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		MailDetailFragment newFragment = MailDetailFragment.newInstance(uri);
		ft.replace(R.id.root_container, newFragment);
		ft.commit();

	}
}
