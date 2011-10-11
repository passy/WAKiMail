package net.rdrei.android.wakimail.guice;

import net.rdrei.android.wakimail.URLConnectionFactory;
import net.rdrei.android.wakimail.URLConnectionImpl;
import android.app.ProgressDialog;
import roboguice.config.AbstractAndroidModule;

public class WAKiMailModule extends AbstractAndroidModule {

	@Override
	protected void configure() {
		bind(ProgressDialog.class).toProvider(ProgressDialogProvider.class);
		bind(URLConnectionFactory.class).to(URLConnectionImpl.class);
	}

}
