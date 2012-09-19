package net.rdrei.android.wakimail.test;

import java.util.Map;
import java.util.Set;

import net.rdrei.android.wakimail.data.MailPreferences;
import net.rdrei.android.wakimail.guice.ActionBarProvider;
import net.rdrei.android.wakimail.ui.DashboardActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.RoboGuice;
import android.app.ActionBar;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.inject.AbstractModule;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.matchers.StartedMatcher;

@RunWith(InjectedTestRunner.class)
public class DashboardSavedActivityTest {

	private class SavedSharedPreferences implements SharedPreferences {

		public static final String EMAIL = "test@example.com";
		public static final String NAME = "Lewis Brandley";
		public static final String PASSWORD = "pw1234";
		public static final String SESSIONID = "2145";

		@Override
		public boolean contains(String arg0) {
			return arg0.equals(MailPreferences.USER_EMAIL);
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

		@Override
		public Set<String> getStringSet(String arg0, Set<String> arg1) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private DashboardActivity mActivity;

	@Test
	public void savedPreferencesSkipDashboard() {
		mActivity = new DashboardActivity();
		mActivity.onCreate(null);
		final ComponentName componentName = new ComponentName(
				"net.rdrei.android.wakimail",
				"net.rdrei.android.wakimail.ui.MailListActivity");
		final Intent intent = new Intent();
		intent.setComponent(componentName);
		Assert.assertThat(mActivity, new StartedMatcher(intent));
	}

	@Before
	public void setUp() {
		// Override the SharedPreferences injection with our mock.
		
		final AbstractModule module = new AbstractModule() {
			
			@Override
			protected void configure() {
				bind(SharedPreferences.class).toInstance(
						new SavedSharedPreferences());
				bind(ActionBar.class).toProvider(ActionBarProvider.class);
			}
		};
		
		final Application app = Robolectric.application;
		RoboGuice.setBaseApplicationInjector(
				app,
				RoboGuice.DEFAULT_STAGE,
				Modules.override(RoboGuice.newDefaultRoboModule(app)).with(module));
	}

}
