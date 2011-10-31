package net.rdrei.android.wakimail.test;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.matchers.StartedMatcher;

import roboguice.RoboGuice;

import net.rdrei.android.wakimail.data.MailPreferences;
import net.rdrei.android.wakimail.ui.DashboardActivity;
import net.rdrei.android.wakimail.ui.MailListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;

@RunWith(InjectedTestRunner.class)
public class DashboardSavedActivityTest {

	private class MySavedSharedPreferencesApplication extends AbstractModule {
		@Override
		protected void configure() {
			bind(SharedPreferences.class).toInstance(
					new SavedSharedPreferences());

		}
	}

	private class SavedSharedPreferences implements SharedPreferences {

		public static final String EMAIL = "test@example.com";
		public static final String NAME = "Lewis Brandley";
		public static final String PASSWORD = "pw1234";
		public static final String SESSIONID = "2145";

		@Override
		public boolean contains(String arg0) {
			return (arg0 == MailPreferences.USER_EMAIL);
		}

		@Override
		public Editor edit() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<String, ?> getAll() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean getBoolean(String arg0, boolean arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public float getFloat(String arg0, float arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getInt(String arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getLong(String arg0, long arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getString(String arg0, String arg1) {
			if (arg0.equals(MailPreferences.USER_EMAIL)) {
				return EMAIL;
			}
			if (arg0.equals(MailPreferences.USER_NAME)) {
				return NAME;
			}
			if (arg0.equals(MailPreferences.USER_PASSWORD)) {
				return PASSWORD;
			}
			if (arg0.equals(MailPreferences.USER_SESSIONID)) {
				return SESSIONID;
			}
			return arg1;
		}

		@Override
		public void registerOnSharedPreferenceChangeListener(
				OnSharedPreferenceChangeListener arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void unregisterOnSharedPreferenceChangeListener(
				OnSharedPreferenceChangeListener arg0) {
			// TODO Auto-generated method stub

		}

	}

	@Inject
	private DashboardActivity mActivity;

	@Test
	public void savedPreferencesSkipDashboard() {
		mActivity.onCreate(null);
		ComponentName componentName = new ComponentName(
				"net.rdrei.android.wakimail",
				"net.rdrei.android.wakimail.ui.MailListActivity");
		Intent intent = new Intent();
		intent.setComponent(componentName);
		Assert.assertThat(mActivity, new StartedMatcher(intent));
	}

	@Before
	public void setUp() {
		// Override the SharedPreferences injection with our mock.
		RoboGuice
				.setBaseApplicationInjector(
						Robolectric.application,
						RoboGuice.DEFAULT_STAGE,
						Modules.override(
								RoboGuice
										.newDefaultRoboModule(Robolectric.application))
								.with(new MySavedSharedPreferencesApplication()));
	}

}
