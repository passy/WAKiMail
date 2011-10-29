package net.rdrei.android.wakimail.ui;

import com.google.inject.Inject;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.wak.SessionManager;
import net.rdrei.android.wakimail.wak.User;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
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
	private User mUser;

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
			this.mUser = (User) extras
					.getSerializable(LoginActivity.USER_EXTRA_KEY);

			this.mHelloText.setText("You were logged in. Hello, "
					+ this.mUser.getName() + "!");
			this.mSignInButton.setEnabled(false);
			this.mShowMailButton.setEnabled(true);
		}
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
				// XXX: Is this a good idea?
				DashboardActivity.this.mSessionManager.setUserCredentials(
						DashboardActivity.this.mUser.getEmail(),
						DashboardActivity.this.mUser.getPassword());

				final Intent intent = new Intent(DashboardActivity.this,
						MailListActivity.class);
				startActivity(intent);
			}
		});
	}
}
