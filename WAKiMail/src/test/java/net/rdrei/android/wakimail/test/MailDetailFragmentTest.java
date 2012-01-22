package net.rdrei.android.wakimail.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import net.rdrei.android.wakimail.ui.MailDetailFragment;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

import com.google.inject.Inject;

@RunWith(InjectedTestRunner.class)
public class MailDetailFragmentTest {
	
	@Inject MailDetailFragment mFragment;
	
	private class LoaderManagerMock extends LoaderManager {

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
		
	};
	
	@Test
	public void fragmentShouldLoad() {
		MailDetailFragment fragment = spy(mFragment);
		when(fragment.getLoaderManager()).thenReturn(new LoaderManagerMock());
		
		fragment.onActivityCreated(null);
		
		verify(fragment).setListAdapter((SimpleCursorAdapter)anyObject());
	}

}
