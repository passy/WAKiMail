package net.rdrei.android.wakimail.task;

import java.util.Iterator;

import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.data.MailTable;
import net.rdrei.android.wakimail.wak.Mail;
import net.rdrei.android.wakimail.wak.MailListLoader;
import net.rdrei.android.wakimail.wak.MailListLoaderFactory;
import net.rdrei.android.wakimail.wak.SessionManager;
import net.rdrei.android.wakimail.wak.User;
import roboguice.util.Ln;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import com.google.inject.Inject;

/**
 * This task syncs the online mails with the local data store. The
 * {@link SessionManager} is used for authentication. The resulting integer
 * indicates the number of new elements added to the data store and is used
 * to inform the user and is communicated via the handler to the UI thread.
 * 
 * @author pascal
 * 
 */
public class MailSyncTask extends RdreiAsyncTask<Integer> {

	private static final String[] PROJECTION_ALL = {
		MailTable.Columns._ID,
		MailTable.Columns.TITLE,
		MailTable.Columns.SENDER
	};
	
	private static final String[] PROJECTION_EXTERNAL_ID = {
		MailTable.Columns.EXTERNAL_ID
	};
	
	@Inject
	private MailListLoaderFactory mailListLoaderFactory;

	private User user;

	public MailSyncTask(Context context, Handler handler, User user) {
		super(context, handler);
		this.user = user;
	}

	@Override
	public Integer call() throws Exception {
		final MailListLoader loader = mailListLoaderFactory.create(this.user);
		return this.syncMail(loader);
	}
	
	@Override
	protected void onSuccess(Integer result) throws Exception {
		super.onSuccess(result);
		
		if (result > 0) {
			showResultToast(result);
		}
	}

	private void showResultToast(Integer result) {
		final String text = this.formatResourceString(
				R.string.refresh_success, result);
		Toast toast = Toast
				.makeText(this.context, text, Toast.LENGTH_SHORT);
		toast.show();
	}

	private String getLastExternalId(final ContentResolver contentResolver) {
		final Uri uri = MailTable.ALL_MAILS_URI;
		final Cursor lastCursor = contentResolver.query(uri,
				PROJECTION_EXTERNAL_ID, null, null,
				// Add a LIMIT of 1 to the order.
				MailTable.Columns.EXTERNAL_ID + " DESC LIMIT 1");

		final String lastId;
		if (lastCursor.moveToFirst()) {
			// Get the only column queried if available.
			lastId = lastCursor.getString(0);
		} else {
			// Compare to a string of 0, which should always fail.
			// Not an empty string, because this is easier to debug.
			lastId = "0";
		}

		return lastId;
	}

	private int syncMail(Iterable<Mail> iterator) {
		// Get the resolver we need to query the database.
		final ContentResolver contentResolver = this.context
				.getContentResolver();

		// Get the external ID of the last mail we have in the DB.
		final String lastExternalId = getLastExternalId(contentResolver);
		int count = 0;
		Ln.d("Last synced external ID is " + lastExternalId + ".");

		for (Mail mail : iterator) {
			if (mail.getId().equals(lastExternalId)) {
				Ln.d("Matching ID found. Stopping sync.");
				break;
			}

			contentResolver.insert(MailTable.ALL_MAILS_URI, mail.getValues());
			count += 1;
		}

		Ln.d("Synced mail count: " + count);
		return count;
	}
}
