package net.rdrei.android.wakimail.ui;

import net.rdrei.android.wakimail.R;
import roboguice.activity.RoboFragmentActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;

public class MailDetailActivity extends RoboFragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Uri uri = this.getIntent().getData();

		final ActionBar actionBar = this.getSupportActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

		setContentView(R.layout.activity_singlepane_empty);

		// First-time init; create fragment to embed in activity.
		final FragmentTransaction ft = getSupportFragmentManager()
				.beginTransaction();
		final MailDetailFragment newFragment = MailDetailFragment
				.newInstance(uri);
		ft.replace(R.id.root_container, newFragment);
		ft.commit();
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
