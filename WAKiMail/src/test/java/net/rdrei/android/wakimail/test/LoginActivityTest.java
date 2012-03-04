package net.rdrei.android.wakimail.test;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.ui.LoginActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

@RunWith(InjectedTestRunner.class)
public class LoginActivityTest {
	private LoginActivity activity;

	@Before
	public void createActivity() {
		activity = new LoginActivity();
		activity.onCreate(null);
	}

	@Test
	public void hasButtonText() {
		final Resources r = activity.getResources();
		final String s = r.getString(R.string.login_signin);
		Assert.assertEquals("Sign In", s);
	}

	@Test
	public void shouldActivateSignInButtonIfCredentialsProvided() {
		final EditText emailText = (EditText) this.findView(R.id.login_email);
		final EditText passwordText = (EditText) this.findView(R.id.login_password);
		final Button signInBtn = (Button) this.findView(R.id.login_login_btn);

		emailText.setText("hello@example.com");
		passwordText.setText("world");

		Assert.assertTrue(signInBtn.isEnabled());
	}

	private View findView(int viewId) {
		return activity.findViewById(viewId);
	}
}
