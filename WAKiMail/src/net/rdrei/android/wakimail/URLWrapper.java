package net.rdrei.android.wakimail;

import java.io.IOException;
import java.net.URLConnection;

public interface URLWrapper {

	public abstract URLConnection openConnection() throws IOException;

}