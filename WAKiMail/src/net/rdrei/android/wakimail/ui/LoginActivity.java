package net.rdrei.android.wakimail.ui;
import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.task.LoginTask;
import net.rdrei.android.wakimail.wak.LoginManager;
import net.rdrei.android.wakimail.wak.User;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends RoboActivity {
	
	private class LoginTaskResultHandlerCallback implements
			Handler.Callback {

		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == LoginTask.LOGIN_SUCCESSFUL_MESSAGE) {
				Bundle data = msg.getData();
				User user = (User) data.getSerializable("user");
				
				finishWithUser(user);
				
				// I didn't find the documentation, but I guess this indicates
				// that the message requires no further processing and needn't
				// be bubbled up.
				return true;
			}
			return false;
		}
		
	}
	
	private class SignInButtonWatcher implements TextWatcher {
		
		@Override
		public void afterTextChanged(Editable s) {
			signInButton.setEnabled(areRequiredValuesProvided());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}
	
	@InjectView(R.id.login_email)
	private EditText emailEdit;
	
	@InjectView(R.id.login_password)
	private EditText passwordEdit;
	
	@InjectView(R.id.login_login_btn)
	private Button signInButton;
	
	private boolean areRequiredValuesProvided() {
		return this.emailEdit.length() > 0 &&
				this.passwordEdit.length() > 0;
	}
	
	private void login() {
    	String email = this.emailEdit.getText().toString();
    	String password = this.passwordEdit.getText().toString();
    	
    	LoginManager manager = new LoginManager(email, password);
		Ln.d("Starting login operation.");
		
		Handler handler = new Handler(new LoginTaskResultHandlerCallback());
		LoginTask loginTask = new LoginTask(this, handler, manager);
		loginTask.execute();
    }
    
    public void onCancel(View v) {
    	Ln.d("Canceling login action.");
    	this.setResult(RESULT_CANCELED);
    	this.finish();
    }
    
    private void finishWithUser(User user) {
    	Ln.d("Finishing login action with user.");
    	Intent intent = new Intent();
    	intent.putExtra("net.rdrei.android.wakimail.User", user);
    	this.setResult(RESULT_OK, intent);
    	this.finish();
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
    
    public void onSubmit(View v) {
    	this.login();
    }
}