package net.rdrei.android.wakimail.test;

import java.security.NoSuchAlgorithmException;

import junit.framework.Assert;

import net.rdrei.android.wakimail.wak.PassphraseGenerator;

import org.junit.Test;

public class HashGenerationTest {
	/**
	 * Test for build the hash used for login.
	 * @throws NoSuchAlgorithmException 
	 */
	@Test
	public void hashGenerationShouldMatchWeb() throws NoSuchAlgorithmException {
		PassphraseGenerator pw = new PassphraseGenerator(
				"pascal.hartig@berufsakademie-sh.de", "supersecretxxx",
				"4630db5188dbc965da38de461084ab64");
		
		String result = pw.generate();
		Assert.assertEquals("880e4c0ad47e3b8caa51182e7a4f7185", result);
	}
}
