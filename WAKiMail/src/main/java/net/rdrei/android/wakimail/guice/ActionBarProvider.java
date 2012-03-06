package net.rdrei.android.wakimail.guice;

import android.app.Activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ActionBarProvider implements Provider<ActionBar> {
	
	@Inject protected Activity activity;

	@Override
	public ActionBar get() {
		return ((SherlockFragmentActivity)activity).getSupportActionBar();
	}
}
