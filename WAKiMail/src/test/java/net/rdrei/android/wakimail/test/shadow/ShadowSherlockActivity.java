package net.rdrei.android.wakimail.test.shadow;

import android.app.ActionBar;
import android.app.Activity;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowActivity;

@Implements(Activity.class)
public class ShadowSherlockActivity extends ShadowActivity {
	@Implementation
	public ActionBar getActionBar() {
		return new ActionBarStub();
	}
		
	@Implementation
	public void setContentView(int layoutResId) {
		super.setContentView(layoutResId);
	}
}