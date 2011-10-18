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
		
		
		// We could add more URIs here to allow searching within the message
		// bodies and so on.
		
		/**
		 * Body of the email, i.e. the message.
		 */
		public static final String BODY = "body";
		
		/**
		 * The date the email was received.
		 */
		public static final String DATE = "date";
		/**
		 * The default sort order for this table. Most recent mails first.
		 */
		public static final String DEFAULT_SORT_ORDER = "date DESC";
		/**
		 * The ID used on the WAK platform.
		 */
		public static final String EXTERNAL_ID = "external_id";
		/**
		 * The name and address of the sender.
		 */
		public static final String SENDER = "sender";
		/**
		 * The title of the mail.
		 */
		public static final String TITLE = "title";
	}
	

	/**
	 * The content URI to fetch all mails.
	 */
	public static final Uri ALL_MAILS_URI = Uri.parse(
			"content://net.rdrei.android.wakimail.data.mail/mails"
	);
	
	public static final String AUTHORITY = 
			"net.rdrei.android.wakimail.data.mail";
	
	public static final String[] MAILS_PROJECTION = {
		Columns._ID, Columns.TITLE, Columns.DATE, Columns.SENDER
	};
	
	/**
	 * The table used within the database.
	 */
	public static final String TABLE_NAME = "mail";
	
	/**
	 * Do not instantiate this class.
	 */
	private MailTable() {}
}
