package rdrei.net.android.wakimail.test;

import java.io.IOException;
import java.util.ArrayList;

import net.rdrei.android.wakimail.URLConnectionFactory;
import net.rdrei.android.wakimail.wak.Mail;
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
	public void injectMeWithYourPoison() {
		Assert.assertNotNull(urlConnectionFactory);
		Assert.assertNotNull(loader);
		Assert.assertTrue(urlConnectionFactory instanceof FakeURLConnectionFactoryImpl);
	}
	
	@Test
	public void fetchAllMails() throws IOException {
		ArrayList<Mail> mails = this.loader.fetchAllMails();
		Assert.assertEquals(112, mails.size());
		
		// Test a control sample
		Mail mail = mails.get(5);
		Assert.assertEquals("392797", mail.getId());
		Assert.assertEquals("Dirk Marx-Stölting", mail.getSender());
		Assert.assertEquals("Unterrichtstausch", mail.getTitle());
	}
}
