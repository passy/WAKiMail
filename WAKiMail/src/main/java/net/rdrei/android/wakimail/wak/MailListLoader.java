package net.rdrei.android.wakimail.wak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import net.rdrei.android.wakimail.wak.LoginManager.ChallengeException;
import net.rdrei.android.wakimail.wak.LoginManager.LoginException;
import roboguice.util.Ln;

import com.google.inject.Inject;

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
	private String mIteratorResponse = null;

	@Inject
	private SessionManager sessionManager;

	public List<Mail> fetchAllMails() throws IOException, LoginException {
		final String response;
		try {
			response = this.readMailResponse();
		} catch (ChallengeException e) {
			Ln.e(e);
			// Return an empty list.
			return new ArrayList<Mail>();
		}
		final Matcher matcher = MAIL_PATTERN.matcher(response);

		final List<Mail> result = new ArrayList<Mail>();
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

	public void loadResponse() throws IOException, LoginException,
			ChallengeException {
		mIteratorResponse = this.readMailResponse();
	}

	@Override
	public Iterator<Mail> iterator() {
		if (null == mIteratorResponse) {
			throw new IllegalStateException(
					"You need to call loadResponse first in order to use the iterator.");
		}
		final Matcher matcher = MAIL_PATTERN.matcher(mIteratorResponse);
		return new MailIterator(matcher);
	}

	protected String readMailResponse() throws IOException, LoginException,
			ChallengeException {
		final HttpsURLConnection connection = (HttpsURLConnection) this
				.openWAKConnection(MESSAGES_PATH);
		return sessionManager.readConnectionWithSessionCheck(connection);
	}
}
