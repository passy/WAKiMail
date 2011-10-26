package net.rdrei.android.wakimail.ui;

import net.rdrei.android.wakimail.R;
import roboguice.activity.RoboActivity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class MailDetailActivity extends RoboActivity {
	private final static String KEY_URI = "mailDetailURI";
	private Uri uri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_mail_detail);
		
		TextView text = (TextView) this.findViewById(R.id.textView1);
		
		this.uri = this.getIntent().getData();
		
		// Maybe we can get the URI from the saved state?
		if (this.uri == null && savedInstanceState != null) {
			this.uri = Uri.parse(savedInstanceState.getString(KEY_URI));
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// Save the URL in the out state for the resume.
		outState.putString(KEY_URI, this.uri.toString());
	}
}
