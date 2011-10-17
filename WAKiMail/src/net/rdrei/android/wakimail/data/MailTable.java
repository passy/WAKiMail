package net.rdrei.android.wakimail.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This class defined columns and URIs used for Mail storage.
 * @author pascal
 */
public class MailTable {
	/**
	 * Mail definitions.
	 */
	public static final class Columns implements BaseColumns {
		/**
		 * The content URI to fetch all mails.
		 */
		public static final Uri ALL_MAILS_URI = Uri.parse(
				"content://net.rdrei.android.wakimail.data.mail/mails"
		);
		
		// We could add more URIs here to allow searching within the message
		// bodies and so on.
		
		/**
		 * The default sort order for this table. Most recent mails first.
		 */
		public static final String DEFAULT_SORT_ORDER = "date DESC";
		
		public static final String EXTERNAL_ID = "external_id";
		public static final String TITLE = "title";
		public static final String DATE = "date";
		public static final String SENDER = "sender";
		public static final String BODY = "body";
	}
	
	public static final String TABLE_NAME = "mail";
	
	public static final String[] MAILS_PROJECTION = {
		Columns._ID, Columns.TITLE, Columns.DATE, Columns.SENDER
	};
	
	public static final String MAILS_AUTHORITY = 
			"net.rdrei.android.wakimail.data.mail";
}
