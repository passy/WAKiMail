package net.rdrei.android.wakimail.test.shadow;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class LoaderManagerStub extends LoaderManager {

	@Override
	public void destroyLoader(int arg0) {
	}

	@Override
	public void dump(String arg0, FileDescriptor arg1, PrintWriter arg2,
			String[] arg3) {
	}

	@Override
	public <D> Loader<D> getLoader(int arg0) {
		return null;
	}

	@Override
	public <D> Loader<D> initLoader(int arg0, Bundle arg1,
			LoaderCallbacks<D> arg2) {
		return null;
	}

	@Override
	public <D> Loader<D> restartLoader(int arg0, Bundle arg1,
			LoaderCallbacks<D> arg2) {
		return null;
	}
}
