package net.rdrei.android.wakimail.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import net.rdrei.android.wakimail.Constants;
import net.rdrei.android.wakimail.URLWrapper;
import net.rdrei.android.wakimail.URLWrapperFactory;
import net.rdrei.android.wakimail.wak.User;

import com.google.inject.Inject;

public class NetLoader {
	@Inject protected URLWrapperFactory urlWrapperFactory;
	private User user;
	private CookieManager cookieManager;
	
	public NetLoader(User user) {
		super();
		this.user = user;
	}

	protected URLConnection openWAKConnection(String path) throws IOException {
		URLWrapper url = this.urlWrapperFactory.create(
				Constants.URL_BASE + path);
		
		return url.openConnection();
	}
	
	protected void enableUserCookie() {
		final CookieStore store = this.cookieManager.getCookieStore();
		URI cookieURI = null;
		try {
			cookieURI = new URI(Constants.URL_BASE);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}
		final List<HttpCookie> cookies = store.get(cookieURI);
		// The cookie we would like to have in it.
		final HttpCookie cookie = new HttpCookie(Constants.SESSION_COOKIE_NAME,
				this.user.getSessionId());
		if (!cookies.contains(cookie)) {
			store.add(cookieURI, cookie);
		}
	}
	
	protected String readResponseIntoString(HttpsURLConnection connection)
			throws IOException {
		final InputStream inputStream = connection.getInputStream();
		final BufferedReader reader = new BufferedReader(
				new InputStreamReader(inputStream), 2 << 11);
		final StringBuffer buf = new StringBuffer(2 << 11);
		
		String line = reader.readLine();
		while (line != null) {
			buf.append(line);
			line = reader.readLine();
		}
		
		return buf.toString();
	}
	
	/**
	 * Injects the user session cookie into the cookie manager.
	 */
	protected void setDefaultCookieManager() {
		synchronized (this) {
			this.cookieManager = (CookieManager) CookieHandler.getDefault();
			if (this.cookieManager == null) {
				this.cookieManager = new CookieManager();
				CookieHandler.setDefault(this.cookieManager);
			}
		}
	}
}
