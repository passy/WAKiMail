package net.rdrei.android.wakimail.ui;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import net.rdrei.android.wakimail.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends RoboActivity  {
	
	@InjectView(R.id.login_login_btn)
	private Button signInButton;
	
	@InjectView(R.id.login_email)
	private EditText emailEdit;
	
	@InjectView(R.id.login_password)
	private EditText passwordEdit;
	
	private class SignInButtonWatcher implements TextWatcher {
		
		@Override
		public void afterTextChanged(Editable s) {
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
				signInButton.setEnabled(areRequiredValuesProvided());
		}
	}
	
	private boolean areRequiredValuesProvided() {
		return this.emailEdit.length() > 0 &&
				this.passwordEdit.length() > 0;
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        SignInButtonWatcher watcher = new SignInButtonWatcher();
        
        this.emailEdit.addTextChangedListener(watcher);
        this.passwordEdit.addTextChangedListener(watcher);
    }
}