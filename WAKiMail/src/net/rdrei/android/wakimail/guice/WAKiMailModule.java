package net.rdrei.android.wakimail.guice;

import com.google.inject.assistedinject.FactoryProvider;

import net.rdrei.android.wakimail.URLConnectionFactory;
import net.rdrei.android.wakimail.URLConnectionFactoryImpl;
import net.rdrei.android.wakimail.URLWrapper;
import net.rdrei.android.wakimail.URLWrapperFactory;
import net.rdrei.android.wakimail.wak.MailListLoader;
import net.rdrei.android.wakimail.wak.MailListLoaderFactory;
import roboguice.config.AbstractAndroidModule;
import android.app.ProgressDialog;

public class WAKiMailModule extends AbstractAndroidModule {

	@Override
	protected void configure() {
		bind(ProgressDialog.class).toProvider(ProgressDialogProvider.class);
		bind(URLConnectionFactory.class).to(URLConnectionFactoryImpl.class);
		bind(MailListLoaderFactory.class)
			.toProvider(FactoryProvider.newFactory(
				MailListLoaderFactory.class, MailListLoader.class
			)
		);
		bind(URLWrapperFactory.class)
			.toProvider(FactoryProvider.newFactory(
				URLWrapperFactory.class, URLWrapper.class
			)
		);
	}
}
