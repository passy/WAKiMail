package net.rdrei.android.wakimail.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

import net.rdrei.android.wakimail.ui.MailDetailActivity;

@RunWith(InjectedTestRunner.class)
public class SimpleFragmentTest {
	
	@Inject private MailDetailActivity mActivity;
	
	@Test
	public void shouldHaveFragmentManager() {
		Assert.assertNotNull(mActivity.getSupportFragmentManager());
		
	}
}
