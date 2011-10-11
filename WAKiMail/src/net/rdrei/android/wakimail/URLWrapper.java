package net.rdrei.android.wakimail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class URLWrapper {
	@Inject protected URLConnectionFactory urlConnectionFactory;
	private URL url;
	
	@Inject
	public URLWrapper(@Assisted String spec) throws MalformedURLException {
		this.url = new URL(spec);
	}

	public URLConnection openConnection() throws IOException {
		return this.urlConnectionFactory.createInstance(url);
	}

}
