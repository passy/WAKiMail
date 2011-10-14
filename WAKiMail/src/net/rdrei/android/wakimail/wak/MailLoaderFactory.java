package net.rdrei.android.wakimail.wak;

public interface MailLoaderFactory {
	public MailLoader create(User user, String id);
}
