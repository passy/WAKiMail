package net.rdrei.android.wakimail.data;

import net.rdrei.android.wakimail.wak.Mail;
import roboguice.util.Ln;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MailDatabase extends SQLiteOpenHelper {
	/**
	 * Indicates the version number of this database. Will be increased at every
	 * change.
	 */
	private static final int DB_VERSION = 1;

	/**
	 * Name the database is saved as.
	 */
	public static final String DB_NAME = "wakimail.db";

	public MailDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Ln.d("Creating new database.");

		db.execSQL("CREATE TABLE " + MailTable.TABLE_NAME + " ("
				+ MailTable.Columns._ID + " INTEGER PRIMARY KEY, "
				+ MailTable.Columns.EXTERNAL_ID + " TEXT NOT NULL, "
				+ MailTable.Columns.DATE + " INTEGER NOT NULL, "
				+ MailTable.Columns.TITLE + " TEXT NOT NULL, "
				+ MailTable.Columns.SENDER + " TEXT NOT NULL, "
				+ MailTable.Columns.BODY + " TEXT" + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		throw new UnsupportedOperationException(
				"There's no need to upgrade, yet.");
	}

	public long insertMail(Mail mail) {
		ContentValues values = new ContentValues();
		values.put(MailTable.Columns.EXTERNAL_ID, mail.getId());
		values.put(MailTable.Columns.TITLE, mail.getTitle());
		values.put(MailTable.Columns.DATE, mail.getDate().getTimeInMillis());
		values.put(MailTable.Columns.SENDER, mail.getSender());

		// Optional field
		String body = mail.getBody();
		if (body != null) {
			values.put(MailTable.Columns.BODY, body);
		}

		return this.getWritableDatabase().insert(MailTable.TABLE_NAME, null,
				values);
	}

	/**
	 * Return a cursor to the mails, restricted by the limit and offset.
	 * 
	 * @return
	 */
	public Cursor getMails(int limit, int offset) {
		SQLiteDatabase db = this.getReadableDatabase();

		String[] columns = new String[] { MailTable.Columns._ID,
				MailTable.Columns.TITLE, MailTable.Columns.SENDER,
				MailTable.Columns.DATE, MailTable.Columns.BODY,
				MailTable.Columns.EXTERNAL_ID };
		String limitStr = null;
		if (limit > -1 && offset > -1) {
			limitStr = String.format("LIMIT %d OFFSET %d", limit, offset);
		}
		return db.query(MailTable.TABLE_NAME, columns, null, null, null, null,
				limitStr);
	}

	public Cursor getAllMails() {
		return getMails(-1, -1);
	}
}
