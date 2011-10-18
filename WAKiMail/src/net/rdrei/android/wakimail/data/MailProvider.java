package net.rdrei.android.wakimail.data;

import java.util.HashMap;

import roboguice.util.Ln;
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
	private static final int MAILS = 1;
	private static final int MAIL_ID = 2;

	private static final UriMatcher URI_MATCHER;
	private static HashMap<String, String> mailProjectionMap;
	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(MailTable.AUTHORITY, "mails", MAILS);
		URI_MATCHER.addURI(MailTable.AUTHORITY, "mails/*", MAIL_ID);

		mailProjectionMap = new HashMap<String, String>();
		mailProjectionMap.put(MailTable.Columns._ID, MailTable.Columns._ID);
		mailProjectionMap.put(MailTable.Columns.TITLE, MailTable.Columns.TITLE);
		mailProjectionMap.put(MailTable.Columns.SENDER,
				MailTable.Columns.SENDER);
		mailProjectionMap.put(MailTable.Columns.DATE, MailTable.Columns.DATE);
		mailProjectionMap.put(MailTable.Columns.EXTERNAL_ID,
				MailTable.Columns.EXTERNAL_ID);
		mailProjectionMap.put(MailTable.Columns.BODY, MailTable.Columns.BODY);
	}

	private MailDatabase database;

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
		
		if (URI_MATCHER.match(uri) != MAILS) {
			throw new IllegalArgumentException("Invalid insert URI " + uri);
		}

		if (values == null) {
			throw new NullPointerException("values must not be null!");
		}

		SQLiteDatabase db = database.getWritableDatabase();
		rowId = db.insert(MailTable.TABLE_NAME, null, values);

		if (rowId > 0) {
			Uri newUri = ContentUris.withAppendedId(
					MailTable.Columns.ALL_MAILS_URI, rowId);
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}

		throw new IllegalArgumentException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		Ln.d("Creating new MailProvider instance.");
		// Open the database.
		this.database = new MailDatabase(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sort) {

		Ln.d("Received query from URI " + uri);

		// Use the query builder for orm-like access.
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(MailTable.TABLE_NAME);
		queryBuilder.setProjectionMap(mailProjectionMap);
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
		Cursor cursor = queryBuilder.query(db, projection, selection,
				whereArgs, null, null, orderBy);

		// Tell the requester where to watch for changes.
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
