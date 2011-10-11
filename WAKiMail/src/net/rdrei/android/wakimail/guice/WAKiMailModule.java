package net.rdrei.android.wakimail.guice;

import net.rdrei.android.wakimail.URLConnectionFactory;
import net.rdrei.android.wakimail.URLConnectionFactoryImpl;
import roboguice.config.AbstractAndroidModule;
import android.app.ProgressDialog;

public class WAKiMailModule extends AbstractAndroidModule {

	@Override
	protected void configure() {
		bind(ProgressDialog.class).toProvider(ProgressDialogProvider.class);
		bind(URLConnectionFactory.class).to(URLConnectionFactoryImpl.class);
	}
}
