package net.rdrei.android.wakimail.test.shadow;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowActivity;

@Implements(SherlockActivity.class)
public class ShadowSherlockActivity extends ShadowActivity {
	@Implementation
	public ActionBar getSupportActionBar() {
		return new ActionBarStub();
	}
		
	@Implementation
	public void setContentView(int layoutResId) {
		super.setContentView(layoutResId);
	}
}