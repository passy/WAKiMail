package net.rdrei.android.wakimail.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import net.rdrei.android.wakimail.ui.MailListActivity;

@RunWith(InjectedTestRunner.class)
public class MailListActivityTest {
	
	MailListActivity mActivity;
	
	@Before
	public void initializeActivity() {
		mActivity = new MailListActivity();
		mActivity.onCreate(null);
	}
	
	@Test
	public void shouldInitialize() {
		Assert.assertNotNull(mActivity);
	}
}
