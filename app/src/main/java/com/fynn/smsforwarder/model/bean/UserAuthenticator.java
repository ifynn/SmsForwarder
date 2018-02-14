package com.fynn.smsforwarder.model.bean;

import javax.mail.Authenticator;

/**
 * @author Fynn
 */
public class UserAuthenticator extends Authenticator {

    private String userName;
	private String password;

	public UserAuthenticator() {}   

	public UserAuthenticator(String username, String password) {
		this.userName = username;    
		this.password = password;    
	}
}