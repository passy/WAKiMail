package net.rdrei.android.wakimail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class URLWrapperImpl implements URLWrapper {
	@Inject private URLConnectionFactory mUrlConnectionFactory;
	private final URL url;
	
	@Inject
	public URLWrapperImpl(@Assisted String spec) throws MalformedURLException {
		url = new URL(spec);
	}

	/* (non-Javadoc)
	 * @see net.rdrei.android.wakimail.URLWrapper#openConnection()
	 */
	@Override
	public URLConnection openConnection() throws IOException {
		return mUrlConnectionFactory.create(url);
	}

}
