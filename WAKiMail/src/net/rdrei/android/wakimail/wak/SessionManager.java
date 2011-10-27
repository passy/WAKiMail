package net.rdrei.android.wakimail.wak;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import net.rdrei.android.wakimail.Constants;
import net.rdrei.android.wakimail.task.LoginTask;
import net.rdrei.android.wakimail.ui.NetLoader;
import net.rdrei.android.wakimail.wak.LoginManager.ChallengeException;
import net.rdrei.android.wakimail.wak.LoginManager.LoginException;
import roboguice.util.Ln;
import android.content.Context;
import android.os.Handler;

import com.google.inject.Singleton;

@Singleton
public class SessionManager {
	private User user = new User();

	private static final int AUTH_RETRIES = 5;
	private CookieManager cookieManager;

	public SessionManager() {
		super();
	}

	public User getUser() {
		return this.user;
	}

	public void setUserCredentials(String email, String password) {
		this.user.setEmail(email);
		this.user.setPassword(password);
	}

	public void login(Context context, Handler.Callback callback) {
		if (this.user == null || !this.user.hasCredentials()) {
			throw new NullPointerException("The user credentials have "
					+ "not been set up properly.");
		}

		Ln.d("Starting login operation.");
		final LoginManager manager = new LoginManager(this.user);
		final Handler handler = new Handler(callback);
		final LoginTask task = new LoginTask(context, handler, manager);

		task.execute();
		Ln.d("Login task executed.");
	}

	private HttpURLConnection retrySessionConnect(HttpURLConnection connection,
			int tryCount) throws LoginException, IOException,
			ChallengeException {
		if (tryCount >= SessionManager.AUTH_RETRIES) {
			Ln.w("Too many authentification tries failed, giving up.");
			throw new LoginManager.LoginException(
					"Maximum retry count execeeded.");
		}

		if (SessionManager.hasSessionExpired(connection)) {
			Ln.d("Session expired. Renewing session. Attempt #" + tryCount);

			final LoginManager manager = new LoginManager(this.user);
			final String challenge = manager.retrieveChallenge();
			final User newUser = manager.login(challenge);
			this.user.setSessionId(newUser.getSessionId());

			// Open a new connection and copy the required attributes.
			HttpURLConnection newConnection = SessionManager
					.copyConnection(connection);

			// Recursively call this method again.
			return this.retrySessionConnect(newConnection, tryCount + 1);
		}
		
		return connection;
	}

	/**
	 * Copies a pre-defined list of attributes over to to a new URL connection.
	 * Only works for GET requests right now, obviously.
	 * 
	 * @param connection
	 * @return new {@link HttpURLConnection}
	 * @throws IOException
	 */
	public static HttpURLConnection copyConnection(HttpURLConnection connection)
			throws IOException {
		URL url = connection.getURL();
		HttpURLConnection newConnection = (HttpURLConnection) url
				.openConnection();
		newConnection.setDoInput(connection.getDoInput());
		newConnection.setDoOutput(connection.getDoOutput());

		return newConnection;
	}

	/**
	 * Reads a URLConnection into a String and tries to automatically figure out
	 * if the session has expired and retries using the user credentials for
	 * {@link SessionManager#AUTH_RETRIES} tries.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ChallengeException
	 * @throws LoginException
	 */
	public String readConnectionWithSessionCheck(HttpURLConnection connection)
			throws IOException, LoginException, ChallengeException {

		// This might block for as long as it takes to renew the session.
		connection = this.retrySessionConnect(connection, 0);
		return NetLoader.readResponseIntoString(connection);
	}

	/**
	 * Check whether a response looks like it would not be part of a valid user
	 * session. This is indicated by the portal trying to re-set the session
	 * cookie again despite it being set already.
	 * 
	 * @param connection
	 * @return boolean indicating whether the session is valid
	 */
	public static boolean hasSessionExpired(HttpURLConnection connection) {
		String header = connection.getHeaderField("Set-Cookie");

		if (header != null) {
			return header.contains(Constants.SESSION_COOKIE_NAME + "=");
		}

		return false;
	}
	
	public void enableUserCookie() {
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
