package rdrei.net.android.wakimail.test;

import org.junit.runners.model.InitializationError;

import roboguice.RoboGuice;
import roboguice.inject.ContextScope;
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
        
        // Enter the project context scope in order to access it within
        // the tests.
        Injector injector = RoboGuice.getInjector(application);
        injector.getInstance(ContextScope.class).enter(application);

        injector.injectMembers(test);
    }
}