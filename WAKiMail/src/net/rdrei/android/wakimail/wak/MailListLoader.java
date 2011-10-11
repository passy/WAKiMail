package net.rdrei.android.wakimail.wak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import net.rdrei.android.wakimail.ui.NetLoader;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

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

	@Inject
	public MailListLoader(@Assisted User user) {
		super();
		this.user = user;
	}
	
	public ArrayList<String> fetchAllMails() throws IOException {
		HttpsURLConnection connection = 
				(HttpsURLConnection) this.openWAKConnection(
						this.messagesPath);
		connection.getInputStream();
		
		// TODO: Continue parsing and stuff here.
		
		return null;
	}
}
