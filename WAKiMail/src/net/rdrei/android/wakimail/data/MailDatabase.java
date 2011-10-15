package net.rdrei.android.wakimail.data;

import net.rdrei.android.wakimail.wak.Mail;
import roboguice.util.Ln;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MailDatabase extends SQLiteOpenHelper {
	/**
	 * Indicates the version number of this database. Will be increased at
	 * every change.
	 */
	private static final int DB_VERSION = 1;
	
	private static final String DB_NAME = "wakimail";
	private static final String DB_TABLE_NAME = "mail";
	
	private static final String FIELD_ID = "_id";
	private static final String FIELD_EXTERNAL_ID = "external_id";
	private static final String FIELD_TITLE = "title";
	private static final String FIELD_DATE = "date";
	private static final String FIELD_SENDER = "sender";
	private static final String FIELD_BODY = "body";
	
	public MailDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Ln.d("Creating new database.");
		
		db.execSQL("CREATE TABLE " + DB_TABLE_NAME + " (" +
			FIELD_ID + " INTEGER PRIMARY KEY, " +
			FIELD_EXTERNAL_ID + " TEXT NOT NULL, " +
			FIELD_DATE + " INTEGER NOT NULL, " +
			FIELD_SENDER + " TEXT NOT NULL, " +
			FIELD_BODY + " TEXT" +
			")"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		throw new NotImplementedException();
	}
	
	public long insertMail(Mail mail) {
		ContentValues values = new ContentValues();
		values.put(FIELD_EXTERNAL_ID, mail.getId());
		values.put(FIELD_TITLE, mail.getTitle());
		values.put(FIELD_DATE, mail.getDate().getTimeInMillis());
		values.put(FIELD_SENDER, mail.getSender());
		
		String body = mail.getBody();
		if (body != null) {
			values.put(FIELD_BODY, body);
		}
		
		return this.getWritableDatabase().insert(DB_TABLE_NAME, null, values);
	}
	
	/**
	 * Return a cursor to the mails, restricted by the limit and offset.
	 * @return
	 */
	public Cursor getMails(int limit, int offset) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String[] columns = new String[] {
			FIELD_ID,
			FIELD_BODY,
			FIELD_SENDER,
			FIELD_DATE,
			FIELD_BODY,
			FIELD_EXTERNAL_ID
		};
		String limitStr = null;
		if (limit > -1 && offset > -1) {
			limitStr = String.format("LIMIT %d OFFSET %d", limit, offset);
		}
		return db.query(DB_TABLE_NAME, columns, null, null, null, null,
				limitStr);
	}
	
	public Cursor getAllMails() {
		return getMails(-1, -1);
	}
}
