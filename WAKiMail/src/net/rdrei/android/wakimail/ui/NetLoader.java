package net.rdrei.android.wakimail.ui;

import java.io.IOException;
import java.net.URLConnection;

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
}
