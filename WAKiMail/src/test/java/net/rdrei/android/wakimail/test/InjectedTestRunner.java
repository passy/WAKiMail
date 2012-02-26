package net.rdrei.android.wakimail.test;

import net.rdrei.android.wakimail.WAKiMailApplication;
import net.rdrei.android.wakimail.test.shadow.ShadowFragment;
import net.rdrei.android.wakimail.test.shadow.ShadowFragmentActivity;

import org.junit.runners.model.InitializationError;

import roboguice.RoboGuice;

import com.google.inject.Injector;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

public class InjectedTestRunner extends RobolectricTestRunner {

	public InjectedTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	@Override
	public void prepareTest(final Object test) {
		WAKiMailApplication application = (WAKiMailApplication) Robolectric.application;

		Injector injector = RoboGuice.getInjector(application);
		injector.injectMembers(test);
	}

	@Override
	protected void bindShadowClasses() {
		super.bindShadowClasses();
		
		Robolectric.bindShadowClass(ShadowFragment.class);
		Robolectric.bindShadowClass(ShadowFragmentActivity.class);
	}
}