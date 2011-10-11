package net.rdrei.android.wakimail;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionFactoryImpl implements URLConnectionFactory {
	@Override
	public URLConnection createInstance(URL url) throws IOException {
		return url.openConnection();
	}
}
