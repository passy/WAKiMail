/**
 * Adopted from roboguice (http://code.google.com/p/roboguice/source/browse/roboguice/src/test/java/roboguice/shadow/ShadowFragmentActivity.java)
 * @license Apache License 2.0
 */
package net.rdrei.android.wakimail.test.shadow;

import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowActivity;

@Implements(FragmentActivity.class)
public class ShadowFragmentActivity extends ShadowActivity {

	@Implementation
	public FragmentManager getSupportFragmentManager() {
		return new FragmentManagerStub();
	}

	@Implementation
	public ActionBar getSupportActionBar() {
		return new ActionBarStub();
	}
	
	@Implementation
	public LoaderManager getSupportLoaderManager() {
		return new LoaderManagerStub();
	}
}
