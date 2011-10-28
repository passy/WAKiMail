package rdrei.net.android.wakimail.test;

import net.rdrei.android.wakimail.ui.MailListActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import com.google.inject.Inject;

@RunWith(InjectedTestRunner.class)
@PrepareForTest(MailListActivity.class)
public class MailListActivityTest {

	@Inject
	private MailListActivity mActivity;

	@Rule
	public static PowerMockRule rule = new PowerMockRule();
	
	@Before
	public void setUp() {
		mActivity.onCreate(null);
	}

	@Test
	public void shouldInitialize() {
		MailListActivity activity = PowerMockito.spy(mActivity);
		Mockito.when(activity.getListView()).thenReturn(null);
		
		activity.onCreate(null);
		
		Mockito.verify(activity).getListView();
	}
}
