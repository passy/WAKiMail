package net.rdrei.android.wakimail.ui;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.data.MailTable;
import net.rdrei.android.wakimail.task.MailSyncTask;
import net.rdrei.android.wakimail.wak.User;
import roboguice.activity.RoboListActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MailListActivity extends RoboListActivity {

	private class MailSyncTaskHandlerCallback implements Handler.Callback {

		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == MailSyncTask.SYNC_SUCCESS_MESSAGE) {
				Ln.d("UI thread received sync success message.");
				refreshList();
			}
			return true;
		}
		
	}
	
	private final class OnRefreshClickListener implements View.OnClickListener {
		public void onClick(View v) {
			// TODO: Add loading spinner instead of button.
			Handler handler = new Handler(new MailSyncTaskHandlerCallback());
			MailSyncTask task = new MailSyncTask(
					MailListActivity.this,
					handler,
					MailListActivity.this.user);
			
			Ln.d("Starting mail sync task.");
			task.execute();
		}
	}

	public static final String USER_EXTRA = "user";
	private static final String[] PROJECTION = { MailTable.Columns._ID,
			MailTable.Columns.TITLE, MailTable.Columns.SENDER, };
	private SimpleCursorAdapter adapter;
	private Cursor listCursor;

	@InjectView(R.id.refresh_btn) Button refreshButton;

	@InjectExtra(value = USER_EXTRA) private User user;

	private void bindRefreshButton() {
		refreshButton.setOnClickListener(new OnRefreshClickListener());
	}
	
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
		this.listCursor = managedQuery(uri, PROJECTION, null, null, null);
		this.adapter = new SimpleCursorAdapter(this,
				android.R.layout.two_line_list_item, this.listCursor, new String[] {
						MailTable.Columns.TITLE, MailTable.Columns.SENDER },
				new int[] { android.R.id.text1, android.R.id.text2 }, 0);
		setListAdapter(this.adapter);

		final ListView listView = getListView();
		// XXX: This is broken right now, but will be replaced anyway.
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// When clicked, open the detail view.
				Intent intent = new Intent(MailListActivity.this,
						MailDetailActivity.class);
				intent.putExtra(MailDetailActivity.ID_EXTRA, id);
				MailListActivity.this.startActivity(intent);
			}
		});
	}
	
	private void refreshList() {
		Ln.d("Refreshing list.");
		// XXX: Once again, this is a synchronous operation on the UI thread
		// and should be moved to a seperate async query that reloads
		// afterwards.
		this.listCursor.requery();
		this.adapter.notifyDataSetChanged();
	}
}
