package net.rdrei.android.wakimail.test;

import net.rdrei.android.wakimail.data.MailTable;
import net.rdrei.android.wakimail.ui.MailDetailActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;

import com.google.inject.Inject;

@RunWith(InjectedTestRunner.class)
public class MailDetailActivityTest {
	@Inject
	private MailDetailActivity mActivity;
	
	private static int MAIL_ID = 1;
	
	@Before
	public void setUp() {
		Uri uri = ContentUris.withAppendedId(MailTable.ALL_MAILS_URI,
				MAIL_ID);
		Intent intent = new Intent();
		Assert.assertNotNull(mActivity.getSupportFragmentManager());
		intent.setData(uri);
		mActivity.setIntent(intent);
		mActivity.onCreate(null);
	}
	
	/**
	 * Smoke test for starting the activity using the URI.
	 */
	@Test
	public void testActivityStart() {
		Activity activity = new Activity();
		Uri uri = ContentUris.withAppendedId(MailTable.ALL_MAILS_URI,
				1);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		activity.startActivity(intent);
		// StartedMatcher is not applicable, because it only looks for
		// the intent not the actual activity.
	}
}
