package net.rdrei.android.wakimail.test.shadow;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowActivity;

@Implements(SherlockFragmentActivity.class)
public class ShadowSherlockFragmentActivity extends ShadowActivity {
	@Implementation
	public ActionBar getSupportActionBar() {
		return new ActionBarStub();
	}

	@Implementation
	public void setContentView(int layoutResId) {
		super.setContentView(layoutResId);
	}
	
	@Implementation
	public FragmentManager getSupportFragmentManager() {
		return new FragmentManagerStub();
	}
	
	@Implementation
	public LoaderManager getSupportLoaderManager() {
		return new LoaderManagerStub();
	}
}
