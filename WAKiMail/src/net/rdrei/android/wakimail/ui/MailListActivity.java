package net.rdrei.android.wakimail.ui;

import java.io.IOException;
import java.util.List;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.data.MailTable;
import net.rdrei.android.wakimail.task.MailSyncTask;
import net.rdrei.android.wakimail.wak.LoginManager.LoginException;
import net.rdrei.android.wakimail.wak.Mail;
import net.rdrei.android.wakimail.wak.MailListLoader;
import net.rdrei.android.wakimail.wak.MailListLoaderFactory;
import net.rdrei.android.wakimail.wak.User;
import roboguice.activity.RoboListActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;

public class MailListActivity extends RoboListActivity {

	@InjectExtra(value = USER_EXTRA)
	private User user;
	@InjectView(R.id.refresh_btn)
	Button refreshButton;
	@Inject
	private MailListLoaderFactory mailListLoaderFactory;

	public static final String USER_EXTRA = "user";
	private static final String[] PROJECTION = { MailTable.Columns._ID,
			MailTable.Columns.TITLE, MailTable.Columns.SENDER, };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_list);
		this.bindRefreshButton();

		// This is an action on the main thread and is practically unlimited.
		// I'm not sure for how many mails this will work. It should
		// definitely better be paginated and loaded on a different thread.
		// There's actually a ListLoader-something adapter in Android >= 2.3
		// which should be used.
		
		// Set up the mail display using a SimpleCursorAdapter.
		final Uri uri = MailTable.ALL_MAILS_URI;
		final Cursor cursor = managedQuery(uri, PROJECTION, null, null, null);
		final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.two_line_list_item, cursor, new String[] {
						MailTable.Columns.TITLE, MailTable.Columns.SENDER },
				new int[] { android.R.id.text1, android.R.id.text2 }, 0);
		setListAdapter(adapter);

		final ListView listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				// TODO: Open and load the mail content.
				CharSequence text = ((TextView) view).getText();
				Ln.d("Showing toast: " + text);
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void bindRefreshButton() {
		refreshButton.setOnClickListener(new OnRefreshClickListener());
		// TODO: Add a spinner or some stuff like that.
	}

	/**
	 * <b>Synchronous</b> way to fetch the mails and put them into the adapter.
	 * This is obviously only a proof-of-concept solution and must be put into a
	 * task, service or whatever else.
	 * 
	 * @param adapter
	 */
	private void loadMailsTheStupidWay() {
		final MailListLoader loader = mailListLoaderFactory.create(this.user);
		List<Mail> fetchAllMails = null;

		Ln.d("Starting mail download.");
		try {
			fetchAllMails = loader.fetchAllMails();
		} catch (IOException e) {
			Ln.e(e);
			e.printStackTrace();
			return;
		} catch (LoginException e) {
			Ln.e(e);
			e.printStackTrace();
			return;
		}

		Ln.d(String.format("Fetched a total of %d mails.", fetchAllMails.size()));

		ContentResolver resolver = getContentResolver();
		for (Mail mail : fetchAllMails) {
			resolver.insert(MailTable.ALL_MAILS_URI, mail.getValues());
		}
	}

	private final class OnRefreshClickListener implements View.OnClickListener {
		public void onClick(View v) {
			// TODO: Supply handler!
			MailSyncTask task = new MailSyncTask(MailListActivity.this, null,
					MailListActivity.this.user);
			
			Ln.d("Starting mail sync task.");
			task.execute();
		}
	}
}
