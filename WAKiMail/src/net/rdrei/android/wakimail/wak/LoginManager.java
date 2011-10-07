package net.rdrei.android.wakimail.wak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import roboguice.util.Ln;

public class LoginManager {
	
	private String email;
	private String password;
	
	private final String URL_BASE = "https://www.wak-sh.de/";
	private final String URL_ENCODING = "UTF8";
	
	public class ChallengeException extends Exception {
		private static final long serialVersionUID = 1L;

		public ChallengeException(String detailMessage) {
			super(detailMessage);
		}
	}
	
	public class LoginException extends Exception {
		private static final long serialVersionUID = 1L;

		public LoginException(String detailMessage) {
			super(detailMessage);
		}
		
	}
	
	public LoginManager(String email, String password) {
		this.email = email;
		this.password = password;
	}

	private HttpsURLConnection buildConnection(String path) throws IOException {
		URL url = new URL(URL_BASE + path);
		return (HttpsURLConnection) url.openConnection();
	}
	
	public String retrieveChallenge() throws IOException, ChallengeException {
		HttpsURLConnection connection = this.buildConnection("30.html");
		InputStream stream = connection.getInputStream();
		
		BufferedReader bufferedReader = new BufferedReader(
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
	 * In order to retrieve the current challenge, you got to call the
	 * retrieveChallenge() methode first.
	 * 
	 * @param challenge
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 */
	public User login(String challenge) throws NoSuchAlgorithmException,
		IOException, LoginException {
		
		String passphrase = this.generatePassphrase(challenge);
		Ln.d("Passphrase: " + passphrase);
		
		HttpsURLConnection connection = this.buildConnection(
				"community-login.html");
		// Setting method to POST per default.
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		
		byte[] params = this.getLoginPostParameters(passphrase, challenge);
		Ln.d("Sending POST params for login " + new String(params));
		
		connection.setFixedLengthStreamingMode(params.length);
		OutputStream out = connection.getOutputStream();
		Map<String, List<String>> headerFields = connection.getHeaderFields();
		out.write(params);
		out.close();
		
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(connection.getInputStream()), 2 << 11);
		
		return this.readUserData(bufferedReader);
	}
	
	private User readUserData(BufferedReader bufferedReader)
			throws LoginException, IOException {
		
		String line;
		String debug = "";
		
		do {
			line = bufferedReader.readLine();
			if (line != null) {
				if (line.indexOf("<h3>Anmeldefehler</h3>") >= 0) {
					throw new LoginException("Invalid email/password " +
							"combination!");
				}
				debug += line + "\n";
			}
		} while (line != null);
		
		return null;
	}

	/**
	 * Generates the POST parameters required for submitting the login form.
	 * @param passphrase
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private byte[] getLoginPostParameters(String passphrase,
			String challenge) throws UnsupportedEncodingException {
		HashMap<String, String> values = new HashMap<String, String>();
		
		values.put("user", this.email);
		values.put("pass", passphrase);
		values.put("submit", "Anmelden");
		values.put("logintype", "login");
		values.put("pid", "3");
		values.put("redirect_url", "");
		values.put("challenge", challenge);
		
		return this.generatePostParamsFromMap(values);
	}
	
	private byte[] generatePostParamsFromMap(
			Map<String, String> values)
			throws UnsupportedEncodingException {
		
		Iterable<Entry<String,String>> set = values.entrySet();
		StringBuilder builder = new StringBuilder();
		int count = 0;
		
		for (Entry<String, String> entry : set) {
			if (count > 0) {
				builder.append("&");
			}
			builder.append(URLEncoder.encode(entry.getKey(), URL_ENCODING)
					+ "=");
			builder.append(URLEncoder.encode(entry.getValue(), URL_ENCODING));
			
			count += 1;
		}
		
		return builder.toString().getBytes();
	}

	/**
	 * Generates the passphrase based on password and current challenge.
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	private String generatePassphrase(String challenge) 
			throws NoSuchAlgorithmException {
		MessageDigest phraseMD5 = MessageDigest.getInstance("MD5");
		// Yeah, really. Security by obscurity at it's finest. Unfortunately
		// so easy to reverse-engineer.
		MessageDigest passwordMD5 = MessageDigest.getInstance("MD5");
		
		byte[] passwordHash = passwordMD5.digest(this.password.getBytes());
		phraseMD5.update((this.email + ":").getBytes());
		phraseMD5.update(passwordHash);
		phraseMD5.update(":".getBytes());
		phraseMD5.update(challenge.getBytes());
		return phraseMD5.digest().toString();
	}

	/**
	 * Extract the challenge value from the response.
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
		
		throw new ChallengeException("Challenge not found in page. " +
				"Are you getting pay-walled?");
	}
	
	private String extractChallengeFromLine(String line) {
		Pattern pattern = Pattern.compile("<input type=\"hidden\" " +
				"name=\"challenge\" value=\"([a-z0-9]+)\">");
		Matcher matcher = pattern.matcher(line);
		
		while (matcher.find()) {
			return matcher.group(1);
		}
		
		return "";
	}
}
