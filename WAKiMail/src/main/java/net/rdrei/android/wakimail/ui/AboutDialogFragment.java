package net.rdrei.android.wakimail.ui;

import net.rdrei.android.wakimail.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import roboguice.fragment.RoboDialogFragment;

public class AboutDialogFragment extends RoboDialogFragment {
	
	public static AboutDialogFragment newInstance() {
		AboutDialogFragment dialog = new AboutDialogFragment();
		return dialog;
	}

	/**
	 * Initializes the about dialog.
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = LayoutInflater.from(this.getActivity()).inflate(
				R.layout.dialog_about, null);
		String title = this.getString(R.string.about_title);
		String positiveMessage = this.getString(android.R.string.ok);
		return new AlertDialog.Builder(getActivity()).setTitle(title)
				.setCancelable(true).setIcon(R.drawable.icon)
				.setPositiveButton(positiveMessage, null).setView(view)
				.create();
	}
	
	

}
