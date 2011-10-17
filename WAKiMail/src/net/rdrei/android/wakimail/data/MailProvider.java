package net.rdrei.android.wakimail.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class MailProvider extends ContentProvider {

	public static final Uri CONTENT_URI = Uri
			.parse("content://net.rdrei.net.android.wakimail.data.Mail");

	private MailDatabase database;
	private static final UriMatcher URI_MATCHER;

	private static final int MAILS = 1;
	private static final int MAIL_ID = 2;

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int count;
		SQLiteDatabase db = database.getWritableDatabase();

		switch (URI_MATCHER.match(uri)) {
		case MAILS:
			count = db.delete(MailTable.TABLE_NAME, where, whereArgs);
			break;

		case MAIL_ID:
			if (!TextUtils.isEmpty(where) || (whereArgs != null)) {
				throw new UnsupportedOperationException("Cannot delete "
						+ "using were clause!");
			}

			String rowId = uri.getPathSegments().get(1);
			count = db.delete(MailTable.TABLE_NAME, MailTable.Columns._ID
					+ "=?", new String[] { rowId });
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (URI_MATCHER.match(uri)) {
		case MAILS:
			return "vnd.android.cursor.dir/vnd.rdrei.wakimail";
		case MAIL_ID:
			return "vnd.android.cursor.item/vnd.rdrei.wakimail";
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowId;

		if (values == null) {
			throw new NullPointerException("values must not be null!");
		}

		SQLiteDatabase db = database.getWritableDatabase();
		rowId = db.insert(MailTable.TABLE_NAME, null, values);

		if (rowId > 0) {
			Uri newUri = ContentUris.withAppendedId(
					MailTable.Columns.ALL_MAILS_URI, rowId);
			return newUri;
		}

		throw new IllegalArgumentException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		// Open the database.
		this.database = new MailDatabase(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sort) {

		// Use the query builder for orm-like access.
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(MailTable.TABLE_NAME);
		String[] whereArgs = null;

		switch (URI_MATCHER.match(uri)) {

		case MAILS:
			// Return all mails
			break;

		case MAIL_ID:
			// Search by mail ID
			queryBuilder.appendWhere(MailTable.Columns._ID + "=?");
			whereArgs = new String[] { uri.getLastPathSegment() };
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		String orderBy;
		// Figure out, if we have a valid order.
		if (TextUtils.isEmpty(sort)) {
			orderBy = MailTable.Columns.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sort;
		}

		SQLiteDatabase db = database.getReadableDatabase();
		Cursor cursor = queryBuilder.query(db, null, null, whereArgs, null,
				null, orderBy);

		// We could change this if we ever considered making changes to the
		// mails.
		// cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(MailTable.MAILS_AUTHORITY, "mails", MAILS);
		URI_MATCHER.addURI(MailTable.MAILS_AUTHORITY, "mails/*", MAIL_ID);
	}

}
