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
	private static HashMap<String, String> MailProjectionMap;
	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(MailTable.AUTHORITY, "mails", MAILS);
		URI_MATCHER.addURI(MailTable.AUTHORITY, "mails/*", MAIL_ID);

		MailProjectionMap = new HashMap<String, String>();
		MailProjectionMap.put(MailTable.Columns._ID, MailTable.Columns._ID);
		MailProjectionMap.put(MailTable.Columns.TITLE, MailTable.Columns.TITLE);
		MailProjectionMap.put(MailTable.Columns.SENDER,
				MailTable.Columns.SENDER);
		MailProjectionMap.put(MailTable.Columns.DATE, MailTable.Columns.DATE);
		MailProjectionMap.put(MailTable.Columns.EXTERNAL_ID,
				MailTable.Columns.EXTERNAL_ID);
		MailProjectionMap.put(MailTable.Columns.BODY, MailTable.Columns.BODY);
	}

	private MailDatabase mDatabase;

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int count;
		final SQLiteDatabase db = mDatabase.getWritableDatabase();

		switch (URI_MATCHER.match(uri)) {
		case MAILS:
			count = db.delete(MailTable.TABLE_NAME, where, whereArgs);
			break;

		case MAIL_ID:
			if (!TextUtils.isEmpty(where) || (whereArgs != null)) {
				throw new UnsupportedOperationException("Cannot delete "
						+ "using were clause!");
			}

			final String rowId = uri.getPathSegments().get(1);
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
		final long rowId;

		if (URI_MATCHER.match(uri) != MAILS) {
			throw new IllegalArgumentException("Invalid insert URI " + uri);
		}

		if (values == null) {
			throw new NullPointerException("values must not be null!");
		}

		final SQLiteDatabase db = mDatabase.getWritableDatabase();
		rowId = db.insert(MailTable.TABLE_NAME, null, values);

		if (rowId > 0) {
			final Uri newUri = ContentUris.withAppendedId(MailTable.ALL_MAILS_URI,
					rowId);
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}

		throw new IllegalArgumentException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		Ln.d("Creating new MailProvider instance.");
		// Open the database.
		mDatabase = new MailDatabase(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sort) {

		Ln.d("Received query from URI " + uri);

		// Use the query builder for orm-like access.
		final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(MailTable.TABLE_NAME);
		queryBuilder.setProjectionMap(MailProjectionMap);
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

		final SQLiteDatabase db = mDatabase.getReadableDatabase();
		final Cursor cursor = queryBuilder.query(db, projection, selection,
				whereArgs, null, null, orderBy);

		// Tell the requester where to watch for changes.
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] selectionArgs) {
		if (URI_MATCHER.match(uri) != MAIL_ID) {
			// Actually, we could allow multiple as well, but as long as it's
			// not necessary, I won't allow it.
			throw new IllegalArgumentException(
					"Can only update a single mail at a time.");
		}
		
		if (where == null) {
			where = MailTable.Columns._ID + " = ? ";
			selectionArgs = new String[] { uri.getLastPathSegment() };
		}

		final SQLiteDatabase db = mDatabase.getWritableDatabase();
		final int count = db.update(MailTable.TABLE_NAME, values, where,
				selectionArgs);
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
