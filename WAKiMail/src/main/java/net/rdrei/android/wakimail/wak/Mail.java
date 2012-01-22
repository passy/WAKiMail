package net.rdrei.android.wakimail.wak;

import java.util.Calendar;

import net.rdrei.android.wakimail.data.MailTable;

import android.content.ContentValues;

/**
 * Represents a single mail. The content might need to be asynchronously
 * fetched.
 * @author pascal
 *
 */
public class Mail {
	// Saved as String because we don't use it as 
	private String id;
	private String title;
	private String sender;
	private String body;
	
	private Calendar date;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Calendar getDate() {
		return this.date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
	
	/**
	 * Helper method to set the date from the Date string given on the
	 * web site.
	 * @param date
	 */
	public void setDateFromString(String date) {
		this.date = WAKDateParser.parse(date);
	}

	public Mail() {
	}
	
	public Mail(String id, String title, String sender, String body,
			Calendar date) {
		super();
		this.id = id;
		this.title = title;
		this.sender = sender;
		this.body = body;
		this.date = date;
	}
	
	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		
		values.put(MailTable.Columns.EXTERNAL_ID, this.id);
		values.put(MailTable.Columns.TITLE, this.title);
		values.put(MailTable.Columns.SENDER, this.sender);
		values.put(MailTable.Columns.BODY, this.body);
		values.put(MailTable.Columns.DATE, this.date.getTimeInMillis());
		
		return values;
	}
}
