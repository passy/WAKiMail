package net.rdrei.android.wakimail;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import roboguice.util.Ln;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy;
import android.os.StrictMode.VmPolicy.Builder;

/**
 * Sets up settings that are required for the application to run.
 * 
 * @author pascal
 */
@ReportsCrashes(formUri = "http://www.bugsense.com/api/acra?api_key=2b0a6e7c", formKey = "")
public class WAKiMailApplication extends Application {
	@Override
	public void onCreate() {
		ACRA.init(this);
		super.onCreate();

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

		VmPolicy vmPolicy = null;

		try {
			Builder.class.getMethod("detectLeakedClosableObjects");
		} catch (SecurityException e) {
			Ln.e(e);
			return;
		} catch (NoSuchMethodException e) {
			vmPolicy = new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().penaltyLog().build();
		}

		if (vmPolicy == null) {
			vmPolicy = new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().build();
		}

		StrictMode.setVmPolicy(vmPolicy);
	}
}
