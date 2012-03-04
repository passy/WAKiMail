package net.rdrei.android.wakimail.wak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import net.rdrei.android.wakimail.Constants;
import roboguice.util.Ln;

public class LoginManager {

	public static class ChallengeException extends Exception {
		private static final long serialVersionUID = 1L;

		public ChallengeException(String detailMessage) {
			super(detailMessage);
		}
	}

	public static class LoginException extends Exception {
		private static final long serialVersionUID = 1L;

		public LoginException(String detailMessage) {
			super(detailMessage);
		}

	}

	private final Pattern challengePattern = Pattern
			.compile("<input type=\"hidden\" "
					+ "name=\"challenge\" value=\"([a-z0-9]+)\">");
	private final Pattern userNamePattern = Pattern
			.compile("<b>Hallo&nbsp;(.*?)!</b>");

	private CookieManager cookieManager;
	private String email;

	private String password;

	private static final String URL_ENCODING = "UTF8";

	public LoginManager(String email, String password) {
		this.email = email;
		this.password = password;

		cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
	}

	public LoginManager(User user) {
		this(user.getEmail(), user.getPassword());

		// I have no idea in which state the object is after this exception
		// is thrown. It really shouldn't happen, I guess.
		if (!user.hasCredentials()) {
			throw new IllegalArgumentException("Provided user object did not"
					+ " provide credentials!");
		}
	}

	private HttpsURLConnection buildConnection(String path) throws IOException {
		final URL url = new URL(Constants.URL_BASE + path);
		final URLConnection connection = url.openConnection();
		return (HttpsURLConnection) connection;
	}

	private String extractChallengeFromLine(String line) {
		final Matcher matcher = challengePattern.matcher(line);

		while (matcher.find()) {
			return matcher.group(1);
		}

		return "";
	}

	private String getSessionId() {
		List<HttpCookie> cookies = null;
		try {
			cookies = cookieManager.getCookieStore().get(
					new URI(Constants.URL_BASE));
		} catch (URISyntaxException e) {
			// Constant value, cannot happen at runtime.
			e.printStackTrace();
			return null;
		}

		for (HttpCookie httpCookie : cookies) {
			if (httpCookie.getName().equals(Constants.SESSION_COOKIE_NAME)) {
				return httpCookie.getValue();
			}
		}

		return null;
	}

	private String extractUserName(String line) {
		final Matcher matcher = userNamePattern.matcher(line);

		String userName = null;
		while (matcher.find()) {
			userName = matcher.group(1);
		}

		return userName;
	}

	/**
	 * Generates the passphrase based on password and current challenge.
	 *
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String generatePassphrase(String challenge)
			throws NoSuchAlgorithmException {

		final PassphraseGenerator passphraseGenerator = new PassphraseGenerator(
				email, password, challenge);
		return passphraseGenerator.generate();
	}

	private byte[] generatePostParamsFromMap(Map<String, String> values)
			throws UnsupportedEncodingException {

		final Iterable<Entry<String, String>> set = values.entrySet();
		final StringBuilder builder = new StringBuilder(2 << 11);
		int count = 0;

		for (Entry<String, String> entry : set) {
			if (count > 0) {
				builder.append('&');
			}
			builder.append(URLEncoder.encode(entry.getKey(), URL_ENCODING)
					+ "=");
			builder.append(URLEncoder.encode(entry.getValue(), URL_ENCODING));

			count += 1;
		}

		return builder.toString().getBytes("utf-8");
	}

	/**
	 * Generates the POST parameters required for submitting the login form.
	 *
	 * @param passphrase
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private byte[] getLoginPostParameters(String passphrase, String challenge)
			throws UnsupportedEncodingException {
		// The order might matter, so we use the linked hash map
		// implementation here.
		final Map<String, String> values = new LinkedHashMap<String, String>();

		values.put("user", email);
		values.put("pass", passphrase);
		values.put("submit", "Anmelden");
		values.put("logintype", "login");
		values.put("pid", "3");
		values.put("redirect_url", "");
		values.put("challenge", challenge);

		return this.generatePostParamsFromMap(values);
	}

	private User handleLoginResponse(HttpsURLConnection connection)
			throws IOException, LoginException {

		final int responseCode = connection.getResponseCode();
		switch (responseCode) {
		case HttpURLConnection.HTTP_OK:
			// OK, but means we had an error on login.
			final BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(connection.getInputStream()), 2 << 11);

			try {
				this.raiseLoginErrorFromResponseStream(bufferedReader);
			} finally {
				bufferedReader.close();
			}
			break;
		case HttpURLConnection.HTTP_MOVED_TEMP:
			// Can mean we reached the maximum login attempts or the login
			// did actually work.
			final String location = connection.getHeaderField("Location");

			if (location.equals("/index.php?id=90")) {
				// The success page.
				return this.retrieveUserInformation();
			} else {
				// There could actually be other reasons for this to happen,
				// but this is the most likely case.
				Ln.w("Got a redirect on login to " + location);
				throw new LoginException("Your account was banned for an hour.");
			}
		case HttpURLConnection.HTTP_INTERNAL_ERROR:
			// Server error. The HttpURLConnection may provide additional
			// information.
			final InputStream error = connection.getErrorStream();
			final BufferedReader bufferedErrorReader = new BufferedReader(
				new InputStreamReader(error), 2 << 11
			);
			try {
				this.raiseLoginErrorFromServerErrorResponseStream(
						bufferedErrorReader);
			} finally {
				bufferedErrorReader.close();
			}
			break;
		default:
			// Throw exception below.
		}

		Ln.e("Login failed with code " + responseCode);
		throw new LoginException("The login failed for an "
				+ " unknown reason.");
	}

	/**
	 * In order to retrieve the current challenge, you got to call the
	 * retrieveChallenge() method first.
	 *
	 * @param challenge
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public User login(String challenge) throws IOException, LoginException {

		String passphrase = null;
		try {
			passphrase = this.generatePassphrase(challenge);
		} catch (NoSuchAlgorithmException e) {
			Ln.e(e);
		}
		Ln.d("Passphrase: " + passphrase);

		final HttpsURLConnection connection = this
				.buildConnection("community-login.html");

		// Setting method to POST per default.
		connection.setDoOutput(true);
		// Implied by the above, but more explicit this way.
		connection.setRequestMethod("POST");
		// This is false for 10, but apparently true at least for 14+.
		connection.setInstanceFollowRedirects(false);

		final byte[] params = this.getLoginPostParameters(passphrase, challenge);
		Ln.d("Sending POST params for login " + new String(params));

		connection.setFixedLengthStreamingMode(params.length);

		final OutputStream out = connection.getOutputStream();
		
		try {
			out.write(params);
		} finally {
			out.close();
		}

		return handleLoginResponse(connection);
	}

	private void raiseLoginErrorFromResponseStream(BufferedReader bufferedReader)
			throws LoginException, IOException {

		String line;

		do {
			line = bufferedReader.readLine();
			if (line != null) {
				if (line.indexOf("<h3>Anmeldefehler</h3>") >= 0) {
					throw new LoginException("Invalid email/password "
							+ "combination!");
				}
			}
		} while (line != null);

		throw new LoginException("Unknown login exception.");
	}

	private void raiseLoginErrorFromServerErrorResponseStream(
			BufferedReader bufferedReader) throws LoginException, IOException {

		final StringBuffer response = new StringBuffer();
		String line;

		do {
			line = bufferedReader.readLine();
			if (line != null) {
				response.append(line + "\n");
			}
		} while (line != null);

		Ln.e("WAK server aborted login with error: " + response.toString());
		throw new LoginException("There was a server error while " +
				"logging in.");
	}

	/**
	 * Extract the challenge value from the response.
	 *
	 * @param bufferedReader
	 * @return String
	 * @throws IOException
	 */
	private String readChallenge(BufferedReader bufferedReader)
			throws IOException, ChallengeException {

		String line, challenge;

		do {
			line = bufferedReader.readLine();
			if (line != null) {
				challenge = this.extractChallengeFromLine(line);
				if (challenge.length() > 0) {
					return challenge;
				}
			}
		} while (line != null);

		throw new ChallengeException("Challenge not found in page. "
				+ "Are you getting pay-walled?");
	}

	public String retrieveChallenge() throws IOException, ChallengeException {
		final HttpsURLConnection connection = this.buildConnection("30.html");
		final InputStream stream = connection.getInputStream();

		final BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(stream), 2 << 11);

		String challenge = null;

		try {
			challenge = readChallenge(bufferedReader);
		} finally {
			bufferedReader.close();
			connection.disconnect();
		}

		Ln.d("Found the challenge: " + challenge);
		// This cannot be null, because this would have raised an exception
		// until then.
		return challenge;
	}

	/**
	 * After successful login, try to retrieve the user information from the
	 * main page and verify that the login actually happened or raise an
	 * exception.
	 *
	 * @return User objects, built from the response.
	 * @throws LoginException
	 * @throws IOException
	 */
	private User retrieveUserInformation() throws LoginException, IOException {
		final HttpsURLConnection connection = buildConnection("c_uebersicht.html");

		final InputStream stream = connection.getInputStream();
		final BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));

		String userName = null;
		String line;

		try {
			do {
				line = reader.readLine();
				if (line != null) {
					userName = extractUserName(line);
	
					if (userName != null) {
						break;
					}
				}
			} while (line != null);
		} finally {
			reader.close();
		}

		if (userName == null) {
			throw new LoginException("Could not retrieve user name.");
		}

		final User user = new User(email, userName, getSessionId());
		user.setPassword(password);

		return user;
	}
}
