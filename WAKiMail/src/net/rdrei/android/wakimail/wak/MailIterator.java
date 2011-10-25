package net.rdrei.android.wakimail.wak;

import java.util.Iterator;
import java.util.regex.Matcher;

public class MailIterator implements Iterator<Mail> {
	
	private Matcher matcher;

	/**
	 * Internal constructor, because we require a *very* specific matcher that
	 * really shouldn't be used by anyone else.
	 * @param matcher
	 */
	MailIterator(Matcher matcher) {
		this.matcher = matcher;
	}

	@Override
	public boolean hasNext() {
		// Seriously, I like this a lot.
		return this.matcher.find();
	}

	@Override
	public Mail next() {
		Mail mail = new Mail();
		mail.setId(matcher.group(1));
		mail.setTitle(matcher.group(2));
		mail.setDateFromString(matcher.group(3));
		mail.setSender(matcher.group(4));
		return mail;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
