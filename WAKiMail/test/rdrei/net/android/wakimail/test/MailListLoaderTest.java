package rdrei.net.android.wakimail.test;

import java.io.IOException;

import net.rdrei.android.wakimail.URLConnectionFactory;
import net.rdrei.android.wakimail.wak.MailListLoader;
import net.rdrei.android.wakimail.wak.MailListLoaderFactory;
import net.rdrei.android.wakimail.wak.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;


@RunWith(FakeURLInjectedTestRunner.class)
public class MailListLoaderTest {
	private User user;
	@Inject private MailListLoaderFactory factory;
	@Inject protected URLConnectionFactory urlConnectionFactory;
	private MailListLoader loader;
	
	@Before
	public void makeUser() {
		this.user = new User("pascal.hartig@berufsakademie-sh.de",
				"Pascal Hartig", "12345");
		this.loader = factory.create(user);
	}
	
	@Test
	public void injectMeYourPoison() {
		Assert.assertNotNull(urlConnectionFactory);
		Assert.assertNotNull(loader);
		Assert.assertTrue(urlConnectionFactory instanceof FakeURLConnectionFactoryImpl);
	}
	
	@Test
	public void runAndFail() throws IOException {
		this.loader.fetchAllMails();
	}
}
