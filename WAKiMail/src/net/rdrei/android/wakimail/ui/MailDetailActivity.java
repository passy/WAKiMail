package net.rdrei.android.wakimail.ui;

import net.rdrei.android.wakimail.R;
import roboguice.activity.RoboFragmentActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class MailDetailActivity extends RoboFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Uri uri = this.getIntent().getData();

		setContentView(R.layout.activity_singlepane_empty);

		if (savedInstanceState == null) {
			// First-time init; create fragment to embed in activity.
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			MailDetailFragment newFragment = MailDetailFragment
					.newInstance(uri);
			ft.add(R.id.root_container, newFragment);
			ft.commit();
		}
	}
}
