package net.rdrei.android.wakimail.wak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import roboguice.util.Ln;

public class LoginManager {
	
	private String email;
	private String password;
	
	private final String URL_BASE = "https://www.wak-sh.de/";
	
	public LoginManager(String email, String password) {
		this.email = email;
		this.password = password;
	}

	private HttpsURLConnection buildConnection(String path) throws IOException {
		URL url = new URL(URL_BASE + path);
		return (HttpsURLConnection) url.openConnection();
	}
	
	public User login() throws IOException {
		HttpsURLConnection connection = this.buildConnection("30.html");
		InputStream stream = connection.getInputStream();
		
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(stream), 2 << 11);
		
		String line;
		String challenge = "not found";
		
		try {
			// XXX: Refactor!
			do {
				line = bufferedReader.readLine();
				if (line != null) {
					challenge = this.extractChallange(line);
					if (challenge.length() > 0) {
						break;
					}
				}
			} while (line != null);
	 	} finally {
			bufferedReader.close();
	 		connection.disconnect();
	 	}
		
		Ln.d("Found the challenge: " + challenge);
		
		User dummy = new User("test", "test");
		
		return dummy;
	}
	
	private String extractChallange(String line) {
		Pattern pattern = Pattern.compile("<input type=\"hidden\" " +
				"name=\"challenge\" value=\"([a-z0-9]+)\">");
		Matcher matcher = pattern.matcher(line);
		
		while (matcher.find()) {
			return matcher.group(1);
		}
		
		return "";
	}
}
