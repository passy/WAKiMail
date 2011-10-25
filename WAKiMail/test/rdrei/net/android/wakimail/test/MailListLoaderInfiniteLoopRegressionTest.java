package rdrei.net.android.wakimail.test;

import java.io.IOException;
import java.util.List;

import net.rdrei.android.wakimail.URLConnectionFactory;
import net.rdrei.android.wakimail.guice.WAKiMailModule;
import net.rdrei.android.wakimail.wak.LoginManager.LoginException;
import net.rdrei.android.wakimail.wak.Mail;
import net.rdrei.android.wakimail.wak.MailListLoader;
import net.rdrei.android.wakimail.wak.MailListLoaderFactory;
import net.rdrei.android.wakimail.wak.User;

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
import com.google.inject.Stage;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;


@RunWith(RobolectricTestRunner.class)
public class MailListLoaderInfiniteLoopRegressionTest {
	private User user;
	@Inject private MailListLoaderFactory factory;
	@Inject protected URLConnectionFactory urlConnectionFactory;
	@Inject Application application;
	
	private MailListLoader loader;
	
	@Before
	public void setUp() {
		AbstractModule module = new AbstractModule() {
			@Override
			protected void configure() {
				FakeURLConnectionFactoryImpl urlConnectionFactory = 
						new FakeURLConnectionFactoryImpl("resources/test_nachrichten2.html");
				bind(URLConnectionFactory.class).toInstance(urlConnectionFactory);
			}
		};
		
		Module moduleOverride = Modules.override(new WAKiMailModule()).with(module);
		Application app = Robolectric.application;
		RoboGuice.setApplicationInjector(app, Stage.DEVELOPMENT,
				RoboGuice.createNewDefaultRoboModule(app), moduleOverride);
		
		RoboInjector injector = RoboGuice.getInjector(app);
		injector.injectMembers(this);
		
		this.user = new User("pascal.hartig@berufsakademie-sh.de",
				"Pascal Hartig", "12345");
		this.loader = factory.create(user);
	}
	
	@Test
	public void fetchAllMails() throws IOException, LoginException {
		List<Mail> mails = this.loader.fetchAllMails();
		Assert.assertEquals(118, mails.size());
		
		// Test a control sample
		Mail mail = mails.get(0);
		Assert.assertEquals("402020", mail.getId());
		Assert.assertEquals("Elmar Wiechers", mail.getSender());
		Assert.assertEquals("Nachschreibklausur ReWe", mail.getTitle());
	}
}