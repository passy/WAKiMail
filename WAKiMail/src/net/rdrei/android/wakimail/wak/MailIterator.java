package net.rdrei.android.wakimail.wak;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;

public class MailIterator implements Iterator<Mail> {
	
	private Matcher matcher;

	public MailIterator(Matcher matcher) {
		this.matcher = matcher;
	}

	@Override
	public boolean hasNext() {
		// We can't really tell.
		return true;
	}

	@Override
	public Mail next() {
		if (this.matcher.find()) {
			Mail mail = new Mail();
			mail.setId(matcher.group(1));
			mail.setTitle(matcher.group(2));
			mail.setDateFromString(matcher.group(3));
			mail.setSender(matcher.group(4));
			return mail;
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();

	}

}
