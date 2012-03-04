package net.rdrei.android.wakimail.test;

import java.io.IOException;
import java.util.List;

import net.rdrei.android.wakimail.URLConnectionFactory;
import net.rdrei.android.wakimail.guice.WAKiMailModule;
import net.rdrei.android.wakimail.wak.LoginManager.LoginException;
import net.rdrei.android.wakimail.wak.Mail;
import net.rdrei.android.wakimail.wak.MailListLoader;
import net.rdrei.android.wakimail.wak.MailListLoaderFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;
import android.app.Application;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;


@RunWith(RobolectricTestRunner.class)
public class MailListLoaderInfiniteLoopRegressionTest {
	@Inject private MailListLoaderFactory factory;
	@Inject protected URLConnectionFactory urlConnectionFactory;
	@Inject Application application;
	
	private MailListLoader loader;
	
	@Before
	public void setUp() {
		final AbstractModule module = new AbstractModule() {
			@Override
			protected void configure() {
				final FakeURLConnectionFactoryImpl urlConnectionFactory = 
						new FakeURLConnectionFactoryImpl("/fixtures/test_nachrichten2.html");
				bind(URLConnectionFactory.class).toInstance(urlConnectionFactory);
			}
		};
		
		final Module moduleOverride = Modules.override(new WAKiMailModule()).with(module);
		final Application app = Robolectric.application;
		RoboGuice.setBaseApplicationInjector(app, RoboGuice.DEFAULT_STAGE,
				RoboGuice.newDefaultRoboModule(app), moduleOverride);
		
		final RoboInjector injector = RoboGuice.getInjector(app);
		injector.injectMembers(this);
		
		loader = factory.create();
	}
	
	@Test
	public void fetchAllMails() throws IOException, LoginException {
		final List<Mail> mails = loader.fetchAllMails();
		Assert.assertEquals(118, mails.size());
		
		// Test a control sample
		final Mail mail = mails.get(0);
		Assert.assertEquals("402020", mail.getId());
		Assert.assertEquals("Elmar Wiechers", mail.getSender());
		Assert.assertEquals("Nachschreibklausur ReWe", mail.getTitle());
	}
}
