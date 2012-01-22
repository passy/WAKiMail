package net.rdrei.android.wakimail.test;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.ui.DashboardActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.ComponentName;
import android.content.Intent;
import android.widget.Button;

import com.xtremelabs.robolectric.matchers.StartedMatcher;

@RunWith(InjectedTestRunner.class)
public class DashboardActivityTest {
	DashboardActivity mActivity;
	
	@Before
	public void initializeActivity() {
		mActivity = new DashboardActivity();
		mActivity.onCreate(null);
	}

	@Test
	public void shouldInitialize() {
		Assert.assertNotNull(mActivity);
	}

	@Test
	public void loginButtonStartsLoginActivity() {
		Button button = (Button) mActivity
				.findViewById(R.id.dashboard_sign_btn);
		button.performClick();

		ComponentName cm = new ComponentName("net.rdrei.android.wakimail",
				"net.rdrei.android.wakimail.ui.LoginActivity");
		Intent intent = new Intent();
		intent.setComponent(cm);
		Assert.assertThat(mActivity, new StartedMatcher(intent));
	}
}
