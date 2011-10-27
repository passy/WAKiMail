package net.rdrei.android.wakimail.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;

import net.rdrei.android.wakimail.Constants;
import net.rdrei.android.wakimail.URLWrapper;
import net.rdrei.android.wakimail.URLWrapperFactory;

import com.google.inject.Inject;

public class NetLoader {
	@Inject protected URLWrapperFactory urlWrapperFactory;
	
	protected URLWrapper makeWAKUrl(String path) {
		return this.urlWrapperFactory.create(
			Constants.URL_BASE + path
		);
	}

	protected URLConnection openWAKConnection(String path) throws IOException {
		return this.makeWAKUrl(path).openConnection();
	}
	
	public static String readResponseIntoString(URLConnection connection)
			throws IOException {
		final InputStream inputStream = connection.getInputStream();
		final BufferedReader reader = new BufferedReader(
				new InputStreamReader(inputStream), 2 << 11);
		final StringBuffer buf = new StringBuffer(2 << 12);
		
		int c = reader.read();
		while (c != -1) {
			buf.append((char) c);
			c = reader.read();
		}
		
		return buf.toString();
	}
}
