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
	private Button signInButton;
	@InjectView(R.id.dashboard_show_mail_btn)
	private Button showMailButton;
	@InjectView(R.id.dashboard_hello_text)
	private TextView helloText;
	@InjectView(R.id.dashboard_iampassy_btn)
	private Button iAmPassyButton;
	@Inject
	private SessionManager mSessionManager;

	private static final int LOGIN_REQUEST = 1;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_dashboard);
		this.bindSignInButton();
		this.bindIAmPassyButton();
		this.bindShowMailButton();
	}

	private void bindIAmPassyButton() {
		iAmPassyButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DashboardActivity.this.user = new User(
						"pascal.hartig@berufsakademie-sh.de", "Pascal Hartig",
						"invalid");
				DashboardActivity.this.user.setPassword(getString(R.string.passy_password));
				signInButton.setEnabled(true);
				showMailButton.setEnabled(true);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == LOGIN_REQUEST
				&& resultCode == RoboActivity.RESULT_OK) {
			final Bundle extras = data.getExtras();
			// TODO: Use constant.
			this.user = (User) extras
					.getSerializable("net.rdrei.android.wakimail.User");

			helloText.setText("You were logged in. Hello, " + user.getName()
					+ "!");
			signInButton.setEnabled(false);
			showMailButton.setEnabled(true);
		}
	}

	protected void bindSignInButton() {
		signInButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(DashboardActivity.this,
						LoginActivity.class);
				startActivityForResult(intent, LOGIN_REQUEST);
			}
		});
	}

	protected void bindShowMailButton() {
		showMailButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// XXX: Is this a good idea?
				mSessionManager.setUserCredentials(user.getEmail(),
						user.getPassword());
				
				final Intent intent = new Intent(DashboardActivity.this,
						MailListActivity.class);
				startActivity(intent);
			}
		});
	}
}
