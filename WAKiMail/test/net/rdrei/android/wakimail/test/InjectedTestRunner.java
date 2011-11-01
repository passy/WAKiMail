package net.rdrei.android.wakimail.test;

import org.junit.runners.model.InitializationError;

import roboguice.RoboGuice;
import android.app.Application;

import com.google.inject.Injector;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

public class InjectedTestRunner extends RobolectricTestRunner {

    public InjectedTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    public void prepareTest(Object test) {
        Application application = Robolectric.application;
        
        Injector injector = RoboGuice.getInjector(application);
        injector.injectMembers(test);
    }
}