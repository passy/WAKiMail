package net.rdrei.android.wakimail.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.wak.Mail;
import net.rdrei.android.wakimail.wak.MailListLoader;
import net.rdrei.android.wakimail.wak.MailListLoaderFactory;
import net.rdrei.android.wakimail.wak.User;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import roboguice.activity.RoboListActivity;
import roboguice.inject.InjectExtra;
import roboguice.util.Ln;

public class MailListActivity extends RoboListActivity {

	public static final String USER_EXTRA = "user";
	
	@InjectExtra(value=USER_EXTRA) private User user;
	@Inject private MailListLoaderFactory mailListLoaderFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this,
				R.layout.mail_list_item,
				new String[]{"Hello"});
		setListAdapter(adapter);
		
		this.loadMailsTheStupidWay(adapter);
		
		final ListView listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
					Toast.LENGTH_SHORT).show();
			}
		});
		
		setContentView(R.layout.activity_mail_list);
	}

	/**
	 * <b>Synchronous</b> way to fetch the mails and put them into the
	 * adapter. This is obviously only a proof-of-concept solution and must
	 * be put into a task, service or whatever else.
	 * @param adapter
	 */
	private void loadMailsTheStupidWay(ArrayAdapter<String> adapter) {
		final MailListLoader loader = mailListLoaderFactory.create(this.user);
		List<Mail> fetchAllMails = null;
		
		Ln.d("Starting mail download.");
		try {
			fetchAllMails = loader.fetchAllMails();
		} catch (IOException e) {
			Ln.e(e);
			e.printStackTrace();
			return;
		}
		
		Ln.d(String.format("Fetched a total of %d mails.",
				fetchAllMails.size()));
		for (Mail mail : fetchAllMails) {
			adapter.add(mail.getTitle());
		}
	}
}
