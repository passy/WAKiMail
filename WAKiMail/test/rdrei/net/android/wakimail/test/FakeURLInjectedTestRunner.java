package rdrei.net.android.wakimail.test;

import java.util.List;

import net.rdrei.android.wakimail.URLConnectionFactory;
import net.rdrei.android.wakimail.URLWrapper;
import net.rdrei.android.wakimail.URLWrapperFactory;
import net.rdrei.android.wakimail.WAKiMailApplication;
import net.rdrei.android.wakimail.wak.MailListLoader;
import net.rdrei.android.wakimail.wak.MailListLoaderFactory;

import org.junit.runners.model.InitializationError;

import roboguice.application.RoboApplication;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.assistedinject.FactoryProvider;


public class FakeURLInjectedTestRunner extends InjectedTestRunner {
	
	private class FakeURLModule extends AbstractModule {

		@Override
		protected void configure() {
			bind(URLConnectionFactory.class).to(FakeURLConnectionFactoryImpl.class);
			bind(MailListLoaderFactory.class)
				.toProvider(FactoryProvider.newFactory(
						MailListLoaderFactory.class, MailListLoader.class)
			);
			bind(URLWrapperFactory.class)
				.toProvider(FactoryProvider.newFactory(
					URLWrapperFactory.class, URLWrapper.class
				)
			);
		}
		
	}
	
	private class FakeURLApplication extends WAKiMailApplication {

		@Override
		protected void addApplicationModules(List<Module> modules) {
			modules.add(new FakeURLModule());
		}
		
	}

	public FakeURLInjectedTestRunner(Class<?> testClass)
			throws InitializationError {
		
		super(testClass);
	}

	@Override
	protected RoboApplication getApplication() {
    	return (RoboApplication) new FakeURLApplication();
	}
}
