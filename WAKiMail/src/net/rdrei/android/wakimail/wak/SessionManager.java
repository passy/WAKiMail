package net.rdrei.android.wakimail.wak;

import net.rdrei.android.wakimail.task.LoginTask;
import roboguice.util.Ln;

import android.content.Context;
import android.os.Handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SessionManager {
	@Inject private Context context;
	private User user = new User();
	
	public SessionManager() {
		super();
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUserCredentials(String email, String password) {
		this.user.setEmail(email);
		this.user.setPassword(password);
	}
	
	public void login(Handler.Callback callback) {
		if (this.user == null || !this.user.hasCredentials()) {
			throw new NullPointerException("The user credentials have " +
					"not been set up properly.");
		}
		
		Ln.d("Starting login operation.");
    	final LoginManager manager = new LoginManager(this.user);
    	final Handler handler = new Handler(callback);
    	final LoginTask task = new LoginTask(context, handler, manager);
    	
    	task.execute();
    	Ln.d("Login task executed.");
	}
}
