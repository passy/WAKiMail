package net.rdrei.android.wakimail.wak;

import java.sql.Date;
import java.util.Calendar;

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
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
	
	/**
	 * Helper method to set the date from the Date string given on the
	 * web site.
	 * @param date
	 */
	public void setDate(String date) {
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
}
