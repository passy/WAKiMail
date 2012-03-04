package net.rdrei.android.wakimail;

import java.io.IOException;
import java.net.URLConnection;

public interface URLWrapper {

	URLConnection openConnection() throws IOException;

}