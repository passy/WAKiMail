package net.rdrei.android.wakimail.task;

import roboguice.activity.event.OnDestroyEvent;
import roboguice.event.Observes;
import roboguice.util.Ln;
import net.rdrei.android.wakimail.R;
import net.rdrei.android.wakimail.data.MailTable;
import net.rdrei.android.wakimail.wak.MailLoader;
import net.rdrei.android.wakimail.wak.MailLoaderFactory;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import com.google.inject.Inject;

public class MailLoadTask extends RdreiAsyncTask<Void> {

	@Inject
	private MailLoaderFactory mailLoaderFactory;
	private Uri mUri;

	public static final int LOAD_SUCCESS_MESSAGE = 0;
	public static final int LOAD_ERROR_MESSAGE = -1;

	public MailLoadTask(Context context, Handler handler, Uri uri) {
		super(context, handler);
		this.mUri = uri;
	}

	@Override
	public Void call() throws Exception {
		ContentResolver resolver = getContext().getContentResolver();
		String externalId = getExternalId(resolver);

		MailLoader loader = this.mailLoaderFactory.create(externalId);
		String body = loader.load();
		saveMailBody(resolver, body);

		return null;
	}

	/**
	 * Sends the result back to the main thread.
	 * 
	 * @see roboguice.util.SafeAsyncTask#onSuccess(java.lang.Object)
	 */
	@Override
	protected void onSuccess(Void t) throws Exception {
		super.onSuccess(t);

		this.handler.sendEmptyMessage(LOAD_SUCCESS_MESSAGE);
	}

	@Override
	protected void onException(Exception e) throws RuntimeException {
		super.onException(e);

		if (e instanceof InterruptedException) {
			// Don't handle interruptions as usual exceptions.
			return;
		}
		String text = this.formatResourceString(R.string.login_error,
				e.getMessage());
		Toast toast = Toast.makeText(this.context, text, Toast.LENGTH_SHORT);
		toast.show();

		this.handler.sendEmptyMessage(LOAD_ERROR_MESSAGE);
	}

	protected void onActivityDestroy(@Observes OnDestroyEvent event) {
		// Cancel the task if the activity or fragment, respectively,
		// was destroyed in the meantime.
		Ln.d("Destroying task because activity is about to get destroyed.");
		this.cancel(true);
	}

	private int saveMailBody(ContentResolver resolver, String body) {
		ContentValues values = new ContentValues(1);
		values.put(MailTable.Columns.BODY, body);
		return resolver.update(this.mUri, values, null, null);
	}

	/**
	 * Blocking method to retrieve the external ID based on the URL provided.
	 * 
	 * @return External ID
	 */
	private String getExternalId(ContentResolver resolver) {
		// Query the most recent version of the mail.
		// We don't need transactional management, though. Even if there would
		// be conflicting queries, the worst result would be to save the same
		// result twice.
		Cursor cursor = resolver.query(this.mUri,
				new String[] { MailTable.Columns.EXTERNAL_ID }, null, null,
				null);
		cursor.moveToFirst();
		return cursor.getString(0);
	}
}
