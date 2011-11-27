package net.rdrei.android.wakimail.test;

import junit.framework.Assert;
import net.rdrei.android.wakimail.ui.MailListActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

public class MailListActivityTestCase extends
		ActivityInstrumentationTestCase2<MailListActivity> {

	private MailListActivity mActivity;

	public MailListActivityTestCase() {
		super("net.rdrei.android.wakimail.ui", MailListActivity.class);
	}

	public void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);
		mActivity = getActivity();
	}

	public void testSomething() {
		Button refreshBtn = (Button) mActivity
				.findViewById(net.rdrei.android.wakimail.R.id.refresh_btn);
		
		Assert.assertNotNull(refreshBtn);
	}

}
