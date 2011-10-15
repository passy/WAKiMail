package rdrei.net.android.wakimail.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.rdrei.android.wakimail.URLConnectionFactory;
import net.rdrei.android.wakimail.guice.WAKiMailModule;
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
public class MailListLoaderTest {
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
						new FakeURLConnectionFactoryImpl("resources/test_nachrichten.html");
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
	public void injectMeWithYourPoison() {
		Assert.assertNotNull(urlConnectionFactory);
		Assert.assertNotNull(loader);
		Assert.assertTrue(urlConnectionFactory instanceof FakeURLConnectionFactoryImpl);
	}
	
	@Test
	public void fetchAllMails() throws IOException {
		List<Mail> mails = this.loader.fetchAllMails();
		Assert.assertEquals(112, mails.size());
		
		// Test a control sample
		Mail mail = mails.get(5);
		Assert.assertEquals("392797", mail.getId());
		Assert.assertEquals("Dirk Marx-Stölting", mail.getSender());
		Assert.assertEquals("Unterrichtstausch", mail.getTitle());
	}
}
