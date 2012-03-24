package net.rdrei.android.wakimail.test;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import net.rdrei.android.wakimail.ui.MailDetailFragmentPagerActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectedTestRunner.class)
public class MailDetailFragmentPagerActivityTest {
	
	private MailDetailFragmentPagerActivity mActivity;
	
	@Before
	public void setUp() {
		mActivity = new MailDetailFragmentPagerActivity();
		mActivity.onCreate(null);
	}
	
	@Test
	public void shouldInitialize() {
		assertThat(mActivity, notNullValue());
	}

}
