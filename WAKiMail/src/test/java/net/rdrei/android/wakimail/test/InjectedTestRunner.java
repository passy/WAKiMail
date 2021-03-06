package net.rdrei.android.wakimail.test;

import net.rdrei.android.wakimail.WAKiMailApplication;
import net.rdrei.android.wakimail.test.shadow.ShadowFragment;
import net.rdrei.android.wakimail.test.shadow.ShadowFragmentActivity;
import net.rdrei.android.wakimail.test.shadow.ShadowSherlockActivity;
import net.rdrei.android.wakimail.test.shadow.ShadowSherlockFragmentActivity;

import org.junit.runners.model.InitializationError;

import roboguice.RoboGuice;

import com.google.inject.Injector;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

public class InjectedTestRunner extends RobolectricTestRunner {

	public InjectedTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
		addClassOrPackageToInstrument("com.actionbarsherlock.app.SherlockActivity");
		addClassOrPackageToInstrument("com.actionbarsherlock.app.SherlockFragmentActivity");
	}

	@Override
	public void prepareTest(final Object test) {
		final WAKiMailApplication application = (WAKiMailApplication) Robolectric.application;

		final Injector injector = RoboGuice.getInjector(application);
		injector.injectMembers(test);
	}

	@Override
	protected void bindShadowClasses() {
		super.bindShadowClasses();
		
		Robolectric.bindShadowClass(ShadowFragment.class);
		Robolectric.bindShadowClass(ShadowFragmentActivity.class);
		Robolectric.bindShadowClass(ShadowSherlockActivity.class);
		Robolectric.bindShadowClass(ShadowSherlockFragmentActivity.class);
	}
}