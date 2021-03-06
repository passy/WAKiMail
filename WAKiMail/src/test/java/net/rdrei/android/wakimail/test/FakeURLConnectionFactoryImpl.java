package net.rdrei.android.wakimail.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import net.rdrei.android.wakimail.URLConnectionFactory;


public class FakeURLConnectionFactoryImpl implements URLConnectionFactory {
	
	String streamFixture;
	protected URL url;
	
	/**
	 * Creates a new FakeURL factory that returns the given resource fixture
	 * as stream instead of actually opening a network connection.
	 * 
	 * @param streamFixture
	 */
	public FakeURLConnectionFactoryImpl(String streamFixture) {
		this.streamFixture = streamFixture;
	}

	@Override
	public URLConnection create(URL url) {
		this.url = url;
		return new FakeURLConnection(url, streamFixture);
	}
	
	public URL getURL() {
		return url;
	}
}


/**
 * Fakes an HTTPS connection to always return the messages
 * html file after a successful login without actual network connection.
 * 
 * @author pascal
 */
class FakeURLConnection extends HttpsURLConnection {
	
	private final String streamFixture;

	protected FakeURLConnection(URL url, String streamFixture) {
		super(url);
		this.streamFixture = streamFixture;
	}

	@Override
	public void connect() throws IOException {
		throw new IOException("Hello!");
	}
	
	public InputStream getInputStream() throws IOException {
		// This is were the magic happens.
		final InputStream stream = this.getClass().getResourceAsStream(
				streamFixture);
		
		if (stream == null) {
			throw new IOException("Could not find test fixture " + 
				streamFixture);
		}
		
		return stream;
	}

	@Override
	public String getCipherSuite() {
		return null;
	}

	@Override
	public Certificate[] getLocalCertificates() {
		return null;
	}

	@Override
	public Certificate[] getServerCertificates()
			throws SSLPeerUnverifiedException {
		return null;
	}

	@Override
	public void disconnect() {
	}

	@Override
	public boolean usingProxy() {
		return false;
	}
}

