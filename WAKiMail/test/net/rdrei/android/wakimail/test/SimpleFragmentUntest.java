package net.rdrei.android.wakimail.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

import net.rdrei.android.wakimail.ui.MailDetailActivity;

/**
 * This is not without massive work, as the authors of RoboGuice
 * explained to me:
 * 
 * https://groups.google.com/forum/#!msg/roboguice/aGKT7sqbriA/RlbrXeAGeSUJ
 * 
 * @author pascal
 *
 */
// @RunWith(InjectedTestRunner.class)
public class SimpleFragmentUntest {
	
	@Inject private MailDetailActivity mActivity;
	
	// @Test
	public void shouldHaveFragmentManager() {
		Assert.assertNotNull(mActivity.getSupportFragmentManager());
		
	}
}
