package net.rdrei.android.wakimail.wak;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Generates the WAK login passphrase based on the required attributes
 * user email, user password and challenge.
 * @author pascal
 *
 */
public class PassphraseGenerator {
	private final String email;
	private final String password;
	private final String challenge;
	
	public PassphraseGenerator(String email, String password, String challenge) {
		super();
		this.email = email;
		this.password = password;
		this.challenge = challenge;
	}
	
	public String generate() throws NoSuchAlgorithmException {
		final MessageDigest md5 = MessageDigest.getInstance("MD5");
		
		String hashedPassword = this.hashToHex(md5.digest(
				password.getBytes()));
		// Reusing the same instance.
		md5.reset();
		
		final String toHash = email + ":" + hashedPassword + ":" +
			challenge;
		return this.hashToHex(md5.digest(toHash.getBytes()));
	}
	
	private String hashToHex(byte[] hash) {
		// This way ensures that the zero-padding is correct.
		final BigInteger bi = new BigInteger(1, hash);
		return String.format("%0" + (hash.length << 1) + "x",
				bi);
	}
}
