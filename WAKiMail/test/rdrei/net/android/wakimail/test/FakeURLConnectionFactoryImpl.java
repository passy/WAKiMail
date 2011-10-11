package rdrei.net.android.wakimail.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import net.rdrei.android.wakimail.URLConnectionFactory;


/**
 * Fakes an HTTPS connection to always return the messages
 * html file after a successful login without actual network connection.
 * 
 * @author pascal
 */
class FakeURLConnection extends HttpsURLConnection {

	protected FakeURLConnection(URL url) {
		super(url);
	}

	@Override
	public void connect() throws IOException {
		throw new IOException("Hello!");
	}
	
    public InputStream getInputStream() throws IOException {
    	// This is were the magic happens.
    	return this.getClass().getResourceAsStream("/resources/test_nachrichten.html");
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


public class FakeURLConnectionFactoryImpl implements URLConnectionFactory {

	@Override
	public URLConnection createInstance(URL url) {
		return new FakeURLConnection(url);
	}
}
