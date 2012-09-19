package net.rdrei.android.wakimail.guice;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ActionBarProvider implements Provider<ActionBar> {

	@Inject
	protected Activity activity;

	@Override
	public ActionBar get() {
		return ((FragmentActivity) activity).getActionBar();
	}
}
