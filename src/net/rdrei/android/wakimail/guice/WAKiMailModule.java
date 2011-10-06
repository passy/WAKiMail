package net.rdrei.android.wakimail.guice;

import android.app.ProgressDialog;
import roboguice.config.AbstractAndroidModule;

public class WAKiMailModule extends AbstractAndroidModule {

	@Override
	protected void configure() {
		bind(ProgressDialog.class).toProvider(ProgressDialogProvider.class);

	}

}
