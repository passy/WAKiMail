package rdrei.net.android.wakimail.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import com.google.inject.matcher.Matcher;

import net.rdrei.android.wakimail.ui.MailListActivity;

@RunWith(InjectedTestRunner.class)
public class MailListActivityTest {
	
	@Inject private MailListActivity mActivity;
	
	@Before
	public void setUp() {
		mActivity.onCreate(null);
	}
	
	@Test
	public void shouldInitialize() {
		Assert.assertNotNull(mActivity);
	}
}
