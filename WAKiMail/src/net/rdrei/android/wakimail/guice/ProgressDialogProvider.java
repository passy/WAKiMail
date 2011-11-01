package net.rdrei.android.wakimail.guice;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ProgressDialogProvider implements Provider<ProgressDialog> {
	
	@Inject private Context context;

	@Override
	public ProgressDialog get() {
		return new ProgressDialog(this.context);
	}

}
