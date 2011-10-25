package net.rdrei.android.wakimail.wak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import net.rdrei.android.wakimail.ui.NetLoader;
import net.rdrei.android.wakimail.wak.LoginManager.ChallengeException;
import net.rdrei.android.wakimail.wak.LoginManager.LoginException;
import roboguice.util.Ln;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Loads the list of messages from the web.
 * 
 * @author pascal
 */
public class MailListLoader extends NetLoader implements Iterable<Mail> {

	private static final Pattern MAIL_PATTERN = Pattern.compile(
	// I think it's pretty unbelievable that Java < 7 doesn't
	// support named groups.
	// Group 0: Message ID
			"c_email.html\\?&action=getviewmessagessingle"
					+ "&msg_uid=([0-9]+)&folder=[0-9]*\">" +
					// Group 1: Title
					"([^<]+)</a></td>[^<]*" +
					// Group 2: Date
					"<td>(.+?)</td>[^<]*" +
					// Group 3: Sender
					"<td>([^&]+)&nbsp;", Pattern.DOTALL);

	private static final String MESSAGES_PATH = "c_email.html";

	@Inject
	private SessionManager sessionManager;

	@Inject
	public MailListLoader(@Assisted User user) {
		super(user);
		// this.setDefaultCookieManager();
		// this.enableUserCookie();
	}

	public List<Mail> fetchAllMails() throws IOException, LoginException {
		String response;
		try {
			response = this.readMailResponse();
		} catch (ChallengeException e) {
			Ln.e(e);
			// Return an empty list.
			return new ArrayList<Mail>();
		}
		Matcher matcher = MAIL_PATTERN.matcher(response);

		List<Mail> result = new ArrayList<Mail>();
		while (matcher.find()) {
			Mail mail = new Mail();
			mail.setId(matcher.group(1));
			mail.setTitle(matcher.group(2));
			mail.setDateFromString(matcher.group(3));
			mail.setSender(matcher.group(4));
			result.add(mail);
		}

		return result;
	}

	@Override
	public Iterator<Mail> iterator() {
		String response = null;
		try {
			response = this.readMailResponse();
		} catch (IOException e) {
			Ln.e(e);
		} catch (LoginException e) {
			Ln.e(e);
		} catch (ChallengeException e) {
			Ln.e(e);
		}
		
		if (response == null) {
			// This won't hurt, it will just return no iterable items.
			response = "";
		}
		
		Matcher matcher = MAIL_PATTERN.matcher(response);
		return new MailIterator(matcher);
	}

	protected String readMailResponse() throws IOException, LoginException,
			ChallengeException {
		HttpsURLConnection connection = (HttpsURLConnection) this
				.openWAKConnection(MESSAGES_PATH);
		return sessionManager.readConnectionWithSessionCheck(connection);
	}
}
