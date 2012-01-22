package net.rdrei.android.wakimail;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

/**
 * Sets up settings that are required for the application to run.
 * 
 * @author pascal
 */
@ReportsCrashes(
	formUri = "http://www.bugsense.com/api/acra?api_key=2b0a6e7c",
	formKey = "")
public class WAKiMailApplication extends Application {
	@Override
	public void onCreate() {
		// The following line triggers the initialization of ACRA
		ACRA.init(this);
		super.onCreate();
	}
}
