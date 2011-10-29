package net.rdrei.android.wakimail.ui;

import com.google.inject.Inject;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.data.MailPreferences;
import net.rdrei.android.wakimail.wak.SessionManager;
import net.rdrei.android.wakimail.wak.User;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DashboardActivity extends RoboActivity {

	@InjectView(R.id.dashboard_sign_btn)
	private Button mSignInButton;
	@InjectView(R.id.dashboard_show_mail_btn)
	private Button mShowMailButton;
	@InjectView(R.id.dashboard_hello_text)
	private TextView mHelloText;
	@Inject
	private SessionManager mSessionManager;

	private static final int LOGIN_REQUEST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_dashboard);
		this.bindSignInButton();
		this.bindShowMailButton();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == LOGIN_REQUEST
				&& resultCode == RoboActivity.RESULT_OK) {
			final Bundle extras = data.getExtras();
			final User user = (User) extras
					.getSerializable(LoginActivity.USER_EXTRA_KEY);
			this.saveUserCredentials(user);

			this.mHelloText.setText("You were logged in. Hello, "
					+ user.getName() + "!");
			this.mSignInButton.setEnabled(false);
			this.mShowMailButton.setEnabled(true);
		}
	}

	private void saveUserCredentials(User user) {
		this.mSessionManager.setUser(user);
		SharedPreferences preferences = this.getPreferences(MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(MailPreferences.USER_EMAIL, user.getEmail());
		editor.putString(MailPreferences.USER_PASSWORD, user.getPassword());
		editor.putString(MailPreferences.USER_SESSIONID, user.getSessionId());
		editor.commit();
	}
	
	/**
	 * Loads user credentials and sets the singleton accordingly. Returns null
	 * if the data hasn't been saved yet.
	 * @return User object or null.
	 */
	private User loadUserCredentials() {
		SharedPreferences preferences = this.getPreferences(MODE_PRIVATE);
		
		// The three values are always committed all at once, so we don't
		// need to check for all of them. If one is missing, the user
		// manipulated the storage.
		if (preferences.contains(MailPreferences.USER_EMAIL)) {
			User user = new User();
			user.setEmail(preferences.getString(
					MailPreferences.USER_EMAIL,
					""));
			user.setPassword(preferences.getString(
					MailPreferences.USER_PASSWORD,
					""));
			user.setSessionId(preferences.getString(
					MailPreferences.USER_SESSIONID,
					""));
			
			this.mSessionManager.setUser(user);
		}
		
		return null;
	}

	protected void bindSignInButton() {
		this.mSignInButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(DashboardActivity.this,
						LoginActivity.class);
				startActivityForResult(intent, LOGIN_REQUEST);
			}
		});
	}

	protected void bindShowMailButton() {
		this.mShowMailButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(DashboardActivity.this,
						MailListActivity.class);
				startActivity(intent);
			}
		});
	}
}
