package net.rdrei.android.wakimail;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.StrictMode;

import com.bugsense.trace.BugSenseHandler;

/**
 * Sets up settings that are required for the application to run.
 * 
 * @author pascal
 */
public class WAKiMailApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		
		BugSenseHandler.initAndStartSession(this, Constants.BUGSENSE_API_KEY);

		if (isDebuggable()) {
			enableStrictMode();
		}
	}

	private boolean isDebuggable() {
		final int applicationFlags = this.getApplicationInfo().flags;
		return (applicationFlags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
	}

	private void enableStrictMode() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectAll().penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
				.penaltyLog().build());
	}
}
