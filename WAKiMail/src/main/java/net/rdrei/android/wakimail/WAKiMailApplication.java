package net.rdrei.android.wakimail;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.os.StrictMode;

/**
 * Sets up settings that are required for the application to run.
 * 
 * @author pascal
 */
@ReportsCrashes(formUri = "http://www.bugsense.com/api/acra?api_key=2b0a6e7c", formKey = "")
public class WAKiMailApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		if (BuildConfig.DEBUG) {
			enableStrictMode();
		} else {
			// Only if DEBUG is disabled.
			ACRA.init(this);
		}
	}

	private void enableStrictMode() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectAll().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects()
				// Only on 11+ if I'm not mistaken.
				.detectLeakedClosableObjects().penaltyLog().penaltyDeath()
				.build());
	}
}
