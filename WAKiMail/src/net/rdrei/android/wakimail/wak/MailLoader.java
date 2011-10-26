package net.rdrei.android.wakimail.wak;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import net.rdrei.android.wakimail.ui.NetLoader;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Loads a single mail identified by its ID.
 * @author pascal
 *
 */
public class MailLoader extends NetLoader {
	private final String id;
	private static final String MESSAGE_URL = "c_email.html?action=" +
			"getviewmessagessingle&msg_uid=";
	
	private static final Pattern BODY_PATTERN = Pattern.compile(
		"<td valign=\"top\">Nachricht:</td>" +
		"<td>(.+?)</td>",
		Pattern.DOTALL
	);
	
	@Inject
	public MailLoader(@Assisted User user, @Assisted String id) {
		super(user);
		this.id = id;
	}
	
	/**
	 * Synchronous way to load the mail into a String.
	 * @return body of the mail.
	 * @throws IOException 
	 */
	public String load() throws IOException {
		HttpsURLConnection connection = 
				(HttpsURLConnection) this.openWAKConnection(
						MESSAGE_URL + this.id);
		
		String response = NetLoader.readResponseIntoString(connection);
		Matcher matcher = BODY_PATTERN.matcher(response);
		
		if (matcher.find()) {
			return matcher.group(1);
		}
		
		throw new IOException("Could not load mail.");
	}
}
