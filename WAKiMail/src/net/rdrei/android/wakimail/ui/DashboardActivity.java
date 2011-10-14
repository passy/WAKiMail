package net.rdrei.android.wakimail.ui;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.wak.User;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DashboardActivity extends RoboActivity {
	
	@InjectView(R.id.dashboard_sign_btn) Button signInButton;
	@InjectView(R.id.dashboard_show_mail_btn) Button showMailButton;
	@InjectView(R.id.dashboard_hello_text) TextView helloText;
	
	static final int LOGIN_REQUEST = 1;
	private User user;
	
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
		
		if (requestCode == LOGIN_REQUEST && resultCode == RoboActivity.RESULT_OK) {
			Bundle extras = data.getExtras();
			this.user = (User) extras.getSerializable(
					"net.rdrei.android.wakimail.User");
			
			helloText.setText("You were logged in. Hello, " + user.getName() + "!");
			signInButton.setEnabled(false);
		}
	}

	protected void bindSignInButton() {
		signInButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DashboardActivity.this,
						LoginActivity.class);
				startActivityForResult(intent, LOGIN_REQUEST);
			}
		});
	}
	
	protected void bindShowMailButton() {
		showMailButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DashboardActivity.this,
						MailListActivity.class);
				intent.putExtra(MailListActivity.USER_EXTRA,
						DashboardActivity.this.user);
				
				startActivity(intent);
			}
		});
	}
}
