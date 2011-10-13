package net.rdrei.android.wakimail.guice;

import net.rdrei.android.wakimail.URLConnectionFactory;
import net.rdrei.android.wakimail.URLConnectionFactoryImpl;
import net.rdrei.android.wakimail.URLWrapperFactory;
import net.rdrei.android.wakimail.wak.MailListLoaderFactory;
import android.app.ProgressDialog;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class WAKiMailModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ProgressDialog.class).toProvider(ProgressDialogProvider.class);
		bind(URLConnectionFactory.class).to(URLConnectionFactoryImpl.class);
		
		install(new FactoryModuleBuilder()
			.build(MailListLoaderFactory.class)
		);
		install(new FactoryModuleBuilder()
			.build(URLWrapperFactory.class)
		);
	}
}
