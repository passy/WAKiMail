package net.rdrei.android.wakimail.wak;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import net.rdrei.android.wakimail.Constants;
import net.rdrei.android.wakimail.ui.NetLoader;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Loads the list of messages from the web.
 * @author pascal
 */
public class MailListLoader extends NetLoader {
	
	private static final Pattern MAIL_PATTERN = Pattern.compile(
			// I think it's pretty unbelievable that Java < 7 doesn't
			// support named groups.
			// Group 0: Message ID
			"c_email.html\\?&action=getviewmessagessingle" +
			"&msg_uid=([0-9]+)&folder=[0-9]*\">.*?" + 
			// Group 1: Title
			"(.+?)</a></td>.*?" +
			// Group 2: Date
			"<td>(.+?)</td>.*?" +
			// Group 3: Sender
			"<td>([^&]+)&nbsp;</td>", Pattern.MULTILINE | Pattern.DOTALL);
	
	private final String messagesPath = "c_email.html";
	
	private User user;
	private CookieManager cookieManager;

	@Inject
	public MailListLoader(@Assisted User user) {
		super();
		this.user = user;
		this.setDefaultCookieManager();
		this.enableUserCookie();
	}
	
	private void enableUserCookie() {
		CookieStore store = this.cookieManager.getCookieStore();
		URI cookieURI = null;
		try {
			cookieURI = new URI(Constants.URL_BASE);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}
		List<HttpCookie> cookies = store.get(cookieURI);
		// The cookie we would like to have in it.
		HttpCookie cookie = new HttpCookie(Constants.SESSION_COOKIE_NAME,
				this.user.sessionId);
		if (!cookies.contains(cookie)) {
			store.add(cookieURI, cookie);
		}
	}

	public ArrayList<Mail> fetchAllMails() throws IOException {
		// TODO: Setting cookie from user here!!
		HttpsURLConnection connection = 
				(HttpsURLConnection) this.openWAKConnection(
						this.messagesPath);
		
		String response = readResponseIntoString(connection);
		Matcher matcher = MAIL_PATTERN.matcher(response);
		
		ArrayList<Mail> result = new ArrayList<Mail>();
		while (matcher.find()) {
			Mail mail = new Mail();
			mail.setId(matcher.group(1));
			mail.setTitle(matcher.group(2));
			mail.setDate(matcher.group(3));
			mail.setSender(matcher.group(4));
			result.add(mail);
		}
		
		return result;
	}
	
	/**
	 * Injects the user session cookie into the cookie manager.
	 */
	private synchronized void setDefaultCookieManager() {
		this.cookieManager = (CookieManager) CookieHandler.getDefault();
		if (this.cookieManager == null) {
			this.cookieManager = new CookieManager();
			CookieHandler.setDefault(this.cookieManager);
		}
	}
}
