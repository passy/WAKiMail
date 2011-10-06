package net.rdrei.android.wakimail.task;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.wak.LoginManager;
import net.rdrei.android.wakimail.wak.User;
import roboguice.util.Ln;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.google.inject.Inject;


public class LoginTask extends RdreiAsyncTask<User> {
	
	private LoginManager manager;
	
	@Inject protected ProgressDialog dialog;

	public LoginTask(LoginManager manager) {
		super();
		
		this.manager = manager;
	}
	
	@Override
	public User call() throws IOException,
		LoginManager.ChallengeException, NoSuchAlgorithmException {
		String challenge = this.manager.retrieveChallenge();
		// Would be a good point to push back some progress information to the
		// UI thread. This is, however, not supported yet by this interface.
		return this.manager.login(challenge);
	}
	
	@Override
	protected void onPreExecute() {
		dialog.setMessage(this.formatResourceString(
				R.string.login_signing_in));
		dialog.show();
	}
	
	/**
	 * Handle errors during the login and display an error toast.
	 */
	@Override
	protected void onException(Exception err) {
		Ln.w(err);
		
		String errorMessage = this.formatResourceString(R.string.login_error,
				err.getMessage());
		Toast.makeText(this.context, errorMessage, Toast.LENGTH_LONG);
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
}
