package net.rdrei.android.wakimail.wak;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String password;
	private String name;
	private String sessionId;

	public User(String email, String name, String sessionId) {
		super();
		this.email = email;
		this.name = name;
		this.sessionId = sessionId;
	}
	
	public User() {
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", name=" + name + ", sessionId="
				+ sessionId + "]";
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public boolean hasCredentials() {
		return this.email != null && this.password != null;
	}

}
