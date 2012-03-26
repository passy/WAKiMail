package net.rdrei.android.wakimail.test;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import net.rdrei.android.wakimail.ui.MailDetailFragmentPagerActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.net.Uri;

@RunWith(InjectedTestRunner.class)
public class MailDetailFragmentPagerActivityTest {
	
	private MailDetailFragmentPagerActivity mActivity;
	
	@Before
	public void setUp() {
		mActivity = new MailDetailFragmentPagerActivity();
		Intent intent = new Intent();
		intent.setData(Uri.parse("content://wakimail/mail/1"));
		mActivity.setIntent(intent);
		mActivity.onCreate(null);
	}
	
	@Test
	public void shouldInitialize() {
		assertThat(mActivity, notNullValue());
	}

}
