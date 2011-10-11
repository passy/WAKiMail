package net.rdrei.android.wakimail.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import net.rdrei.android.wakimail.Constants;
import net.rdrei.android.wakimail.URLConnectionFactory;
import net.rdrei.android.wakimail.URLWrapper;

import com.google.inject.Inject;

public class NetLoader {
	protected URLConnection openWAKConnection(String path) throws IOException {
		URLWrapper url = null;
		url = new URLWrapper(Constants.URL_BASE + path);
		
		return url.openConnection();
	}
}
