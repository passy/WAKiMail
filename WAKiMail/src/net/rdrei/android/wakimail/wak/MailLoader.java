package net.rdrei.android.wakimail.wak;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import net.rdrei.android.wakimail.ui.NetLoader;

/**
 * Loads a single mail identified by its ID.
 * @author pascal
 *
 */
public class MailLoader extends NetLoader {
	private String id;

	@Inject
	public MailLoader(@Assisted User user, String id) {
		super(user);
		this.id = id;
	}
	
	/**
	 * Synchronous way to load the mail into a String.
	 * @return body of the mail.
	 */
	public String load() {
		return null;
	}
}
