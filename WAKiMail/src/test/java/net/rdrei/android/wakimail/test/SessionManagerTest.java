package net.rdrei.android.wakimail.test;

import net.rdrei.android.wakimail.wak.SessionManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(InjectedTestRunner.class)
public class SessionManagerTest {
	@Inject
	private SessionManager sessionManager;

	@Test
	public void shouldInitialize() {
		assert sessionManager.getUser() == null;
	}
}
