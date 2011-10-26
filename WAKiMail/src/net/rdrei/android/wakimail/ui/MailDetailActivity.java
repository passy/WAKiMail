package net.rdrei.android.wakimail.ui;

import net.rdrei.android.wakimail.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import android.os.Bundle;
import android.widget.TextView;

public class MailDetailActivity extends RoboActivity {
	
	public static final String ID_EXTRA = "_id";
	
	@InjectExtra(ID_EXTRA) private Long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_mail_detail);
		
		TextView text = (TextView) this.findViewById(R.id.textView1);
		text.setText("ID: " + this.id);
	}
}
