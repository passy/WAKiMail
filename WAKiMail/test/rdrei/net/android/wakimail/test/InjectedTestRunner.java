package rdrei.net.android.wakimail.test;

import org.junit.runners.model.InitializationError;

import roboguice.application.RoboApplication;
import roboguice.inject.ContextScope;

import com.google.inject.Injector;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

public class InjectedTestRunner extends RobolectricTestRunner {

    public InjectedTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }
    
    protected RoboApplication getApplication() {
    	return (RoboApplication) Robolectric.application;
    }

    @Override
    public void prepareTest(Object test) {
        RoboApplication application = this.getApplication();

        // Enter the project context scope in order to access it within
        // the tests.
        Injector injector = application.getInjector();
        ContextScope scope = injector.getInstance(ContextScope.class);
        scope.enter(application);

        injector.injectMembers(test);
    }
}