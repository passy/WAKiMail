package net.rdrei.android.wakimail.test.shadow;

import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowActivity;

@Implements(FragmentActivity.class)
public class ShadowSherlockFragmentActivity extends ShadowActivity {
	@Implementation
	public ActionBar getActionBar() {
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
