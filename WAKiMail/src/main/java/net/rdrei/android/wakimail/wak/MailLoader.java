// $codepro.audit.disable unnecessaryImport
package net.rdrei.android.wakimail.wak;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

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
			+ "getviewmessagessingle&msg_uid=%s";

	private static final Pattern BODY_PATTERN = Pattern.compile(
			"<td valign=\"top\">Nachricht:</td>" + "<td>(.+?)</td>",
			Pattern.DOTALL);

	@Inject
	public MailLoader(@Assisted String id) {
		mId = id;
	}

	/**
	 * Synchronous way to load the mail into a String.
	 * 
	 * @return body of the mail.
	 * @throws IOException
	 *             Loading of the mail failed due to an invalid response.
	 * @throws ChallengeException
	 *             The login challenge could not be extracted.
	 * @throws LoginException
	 *             The login failed due to invalid credentials or too many
	 *             failed attempts.
	 */

	public String load() throws IOException, LoginException, ChallengeException {
		final HttpsURLConnection connection = (HttpsURLConnection) this
				.openWAKConnection(String.format(MESSAGE_URL, mId));
		final String response = mSessionManager
				.readConnectionWithSessionCheck(connection);
		final Matcher matcher = BODY_PATTERN.matcher(response);

		if (matcher.find()) {
			return matcher.group(1);
		}

		throw new IOException("Could not load mail.");
	}
}
