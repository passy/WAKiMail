package net.rdrei.android.wakimail.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import net.rdrei.android.wakimail.Constants;
import net.rdrei.android.wakimail.URLWrapper;
import net.rdrei.android.wakimail.URLWrapperFactory;

import com.google.inject.Inject;

public class NetLoader {
	@Inject URLWrapperFactory urlWrapperFactory;
	
	protected URLConnection openWAKConnection(String path) throws IOException {
		URLWrapper url = this.urlWrapperFactory.create(
				Constants.URL_BASE + path);
		
		return url.openConnection();
	}
	
	protected String readResponseIntoString(HttpsURLConnection connection)
			throws IOException {
		InputStream inputStream = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream), 2 << 11);
		StringBuffer buf = new StringBuffer();
		
		String line = reader.readLine();
		while (line != null) {
			buf.append(line);
			line = reader.readLine();
		}
		
		return buf.toString();
	}
}
