package net.rdrei.android.wakimail.test;

import java.io.IOException;
import java.net.URL;

import net.rdrei.android.wakimail.Constants;
import net.rdrei.android.wakimail.URLConnectionFactory;
import net.rdrei.android.wakimail.guice.WAKiMailModule;
import net.rdrei.android.wakimail.wak.LoginManager.ChallengeException;
import net.rdrei.android.wakimail.wak.LoginManager.LoginException;
import net.rdrei.android.wakimail.wak.MailLoader;
import net.rdrei.android.wakimail.wak.MailLoaderFactory;
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
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class MailLoaderTest {
	
	@Inject MailLoaderFactory loaderFactory;
	FakeURLConnectionFactoryImpl urlConnectionFactory;
	User user;
	
	@Before
	public void setUp() {
		AbstractModule module = new AbstractModule() {
			@Override
			protected void configure() {
				urlConnectionFactory = new FakeURLConnectionFactoryImpl(
						"/fixtures/test_email.html");
				bind(URLConnectionFactory.class).toInstance(urlConnectionFactory);
			}
		};
		
		Module moduleOverride = Modules.override(new WAKiMailModule()).with(module);
		Application app = Robolectric.application;
		RoboGuice.setBaseApplicationInjector(app, RoboGuice.DEFAULT_STAGE,
				RoboGuice.newDefaultRoboModule(app), moduleOverride);
		
		RoboInjector injector = RoboGuice.getInjector(app);
		injector.injectMembers(this);
		
		user = new User("pascal.hartig@berufsakademie-sh.de",
				"Pascal Hartig", "12345");
	}
	
	@Test
	public void shouldLoadCorrectMail() throws IOException, LoginException, ChallengeException {
		String id = "1234567";
		MailLoader loader = loaderFactory.create(id);
		
		loader.load();
		URL url = urlConnectionFactory.getURL();
		Assert.assertEquals(
				Constants.URL_BASE +
				"c_email.html?action=getviewmessagessingle&msg_uid=" + id,
				url.toString());
	}
	
	@Test
	public void shouldExtractBody() throws IOException, LoginException, ChallengeException {
		MailLoader loader = loaderFactory.create("123456");
		String body = loader.load();
		
		Assert.assertEquals("Ich würde vorschlagen wir treffen uns nächste " +
				"Woche Montag (17.10.2011) um 15:15 Uhr vor der Aula und " +
				"besprechen wie alles Ablaufen soll.<br>",
				body.split("\n")[1]);
	}
}
