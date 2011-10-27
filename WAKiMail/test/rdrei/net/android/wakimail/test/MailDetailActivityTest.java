package rdrei.net.android.wakimail.test;

import static org.junit.Assert.assertThat;
import net.rdrei.android.wakimail.data.MailTable;
import net.rdrei.android.wakimail.ui.MailDetailActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;

import com.xtremelabs.robolectric.matchers.StartedMatcher;

@RunWith(InjectedTestRunner.class)
public class MailDetailActivityTest {
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
