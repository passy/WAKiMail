package net.rdrei.android.wakimail.test;

import net.rdrei.android.wakimail.ui.DashboardActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(InjectedTestRunner.class)
public class DashboardActivityTest {
	@Inject DashboardActivity mActivity;
	
	@Before
	public void setUpActivity() {
		mActivity.onCreate(null);
	}
	
	@Test
	public void shouldInitialize() {
		Assert.assertNotNull(mActivity);
	}
}
