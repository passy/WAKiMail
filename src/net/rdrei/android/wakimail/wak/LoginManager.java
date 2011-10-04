package net.rdrei.android.wakimail.wak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
		StringBuilder content = new StringBuilder();
		String line;
		
		try {
			// XXX: Refactor!
			do {
				line = bufferedReader.readLine();
				if (line != null) {
					String challange = this.extractChallange(line);
					if (challange != null) {
						break;
					}
				}
			} while(line != null);
	 	} finally {
			bufferedReader.close();
	 		connection.disconnect();
	 	}
		
		Ln.d("Response text: " + content.toString());
		
		User dummy = new User("test", "test");
		
		return dummy;
	}
	
	private String extractChallange(String line) {
		Pattern pattern = new Pattern.compile();
	}
}
