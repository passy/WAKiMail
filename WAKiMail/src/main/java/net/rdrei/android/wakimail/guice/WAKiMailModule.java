package net.rdrei.android.wakimail.guice;

import roboguice.inject.SharedPreferencesName;
import net.rdrei.android.wakimail.URLConnectionFactory;
import net.rdrei.android.wakimail.URLConnectionFactoryImpl;
import net.rdrei.android.wakimail.URLWrapper;
import net.rdrei.android.wakimail.URLWrapperFactory;
import net.rdrei.android.wakimail.URLWrapperImpl;
import net.rdrei.android.wakimail.data.MailPreferences;
import net.rdrei.android.wakimail.wak.MailListLoaderFactory;
import net.rdrei.android.wakimail.wak.MailLoaderFactory;
import android.app.ProgressDialog;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class WAKiMailModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ProgressDialog.class).toProvider(ProgressDialogProvider.class);
		bind(URLConnectionFactory.class).to(URLConnectionFactoryImpl.class);
		
		// Could use a custom scope or extra annotation for this, of course,
		// but we won't have more than one shared data store that needs
		// to be injected.
		bindConstant()
			.annotatedWith(SharedPreferencesName.class)
			.to(MailPreferences.KEY);
		
		install(new FactoryModuleBuilder()
			.build(MailListLoaderFactory.class)
		);
		install(new FactoryModuleBuilder()
			.implement(URLWrapper.class, URLWrapperImpl.class)
			.build(URLWrapperFactory.class)
		);
		install(new FactoryModuleBuilder()
			.build(MailLoaderFactory.class)
		);
	}
}
