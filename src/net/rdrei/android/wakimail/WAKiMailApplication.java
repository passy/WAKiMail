package net.rdrei.android.wakimail;

import java.util.List;

import net.rdrei.android.wakimail.guice.WAKiMailModule;

import com.google.inject.Module;

import roboguice.application.RoboApplication;
import roboguice.util.Ln;

public class WAKiMailApplication extends RoboApplication {

	@Override
	protected void addApplicationModules(List<Module> modules) {
		super.addApplicationModules(modules);
		
		Ln.d("Adding application modules.");
		
		modules.add(new WAKiMailModule());
	}
	
}
