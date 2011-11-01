package net.rdrei.android.wakimail.wak;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import net.rdrei.android.wakimail.ui.NetLoader;
import net.rdrei.android.wakimail.wak.LoginManager.ChallengeException;
import net.rdrei.android.wakimail.wak.LoginManager.LoginException;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Loads a single mail identified by its ID.
 * 
 * @author pascal
 * 
 */
public class MailLoader extends NetLoader {
	@Inject
	private SessionManager mSessionManager;
	private final String mId;
	private static final String MESSAGE_URL = "c_email.html?action="
			+ "getviewmessagessingle&msg_uid=";

	private static final Pattern BODY_PATTERN = Pattern.compile(
			"<td valign=\"top\">Nachricht:</td>" + "<td>(.+?)</td>",
			Pattern.DOTALL);

	@Inject
	public MailLoader(@Assisted String id) {
		this.mId = id;
	}

	/**
	 * Synchronous way to load the mail into a String.
	 * 
	 * @return body of the mail.
	 * @throws IOException
	 * @throws ChallengeException
	 * @throws LoginException
	 */
	public String load() throws IOException, LoginException, ChallengeException {
		final HttpsURLConnection connection = (HttpsURLConnection) this
				.openWAKConnection(MESSAGE_URL + this.mId);
		final String response = this.mSessionManager
				.readConnectionWithSessionCheck(connection);
		Matcher matcher = BODY_PATTERN.matcher(response);

		if (matcher.find()) {
			return matcher.group(1);
		}

		throw new IOException("Could not load mail.");
	}
}
