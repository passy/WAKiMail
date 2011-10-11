package net.rdrei.android.wakimail;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;


public interface URLConnectionFactory {
	public URLConnection createInstance(URL url) throws IOException;
}
