package net.rdrei.android.wakimail.task;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.wak.LoginManager;
import net.rdrei.android.wakimail.wak.User;
import roboguice.util.Ln;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.inject.Inject;


public class LoginTask extends RdreiAsyncTask<User> {
	
	@Inject protected ProgressDialog dialog;
	
	public static final int LOGIN_SUCCESSFUL_MESSAGE = 0;

	private LoginManager manager;
	
	public LoginTask(Handler handler, LoginManager manager) {
		super(handler);
		
		this.manager = manager;
	}

	@Override
	public User call() throws IOException,
		LoginManager.ChallengeException, LoginManager.LoginException,
		NoSuchAlgorithmException {
		
		String challenge = this.manager.retrieveChallenge();
		// Would be a good point to push back some progress information to the
		// UI thread. This is, however, not supported yet by this interface.
		return this.manager.login(challenge);
	}
	
	/**
	 * Handle errors during the login and display an error toast.
	 */
	@Override
	protected void onException(Exception err) {
		Ln.w(err);
		
		String errorMessage = this.formatResourceString(R.string.login_error,
				err.getMessage());
		Ln.d("Error message: " + errorMessage);
		Toast toast = Toast.makeText(this.context, errorMessage, Toast.LENGTH_LONG);
		toast.show();
	}
	
	/**
	 * If the dialog is still visible, hide it.
	 */
	@Override
	protected void onFinally() {
		if (this.dialog.isShowing()) {
			dialog.dismiss();
		}
	}
	
	@Override
	protected void onPreExecute() {
		dialog.setMessage(this.formatResourceString(
				R.string.login_signing_in));
		dialog.show();
	}
	
	@Override
	protected void onSuccess(User user) throws Exception {
		super.onSuccess(user);
		
		String messageStr = this.formatResourceString(R.string.login_success);
		Ln.d("Login successful for User " + user.toString());
		Toast toast = Toast.makeText(this.context, messageStr,
				Toast.LENGTH_SHORT);
		toast.show();
		
		// Tell the parent activity to finish by sending a message containing
		// the user object back to the UI thread over the handler.
		Bundle data = new Bundle();
		data.putSerializable("user", user);
		
		Message message = new Message();
		message.what = LoginTask.LOGIN_SUCCESSFUL_MESSAGE;
		message.setData(data);
		
		this.handler().sendMessage(message);
	}
}
