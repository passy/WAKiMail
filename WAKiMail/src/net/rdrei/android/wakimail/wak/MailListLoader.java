package net.rdrei.android.wakimail.wak;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Pattern;

import net.rdrei.android.wakimail.ui.NetLoader;

/**
 * Loads the list of messages from the web.
 * @author pascal
 */
public class MailListLoader extends NetLoader {
	
	private final Pattern mailPattern = Pattern.compile(
			// I think it's pretty unbelievable that Java < 7 doesn't
			// support named groups.
			// Group 0: Message ID
			"c_email.html?&action=getviewmessagessingle&msg_uid=([0-9]+)\">" + 
			// Group 1: Title
			"(.+?)</a></td>" +
			// Group 2: Date
			"<td>(.+?)</td>" +
			// Group 3: Sender
			"<td>([^&]+)&nbsp;</td>");
	
	private final String messagesPath = "c_email.html";
	
	private User user;

	public MailListLoader(User user) {
		super();
		this.user = user;
	}
	
	public ArrayList<String> fetchAllMails() throws IOException {
		URLConnection connection = this.openWAKConnection(this.messagesPath);
		
		return null;
	}
}
