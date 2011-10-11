package net.rdrei.android.wakimail;

import com.google.inject.assistedinject.Assisted;

public interface URLWrapperFactory {
	public URLWrapper create(String spec);
}
