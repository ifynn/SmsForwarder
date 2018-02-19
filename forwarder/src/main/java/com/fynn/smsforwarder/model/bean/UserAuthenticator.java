package com.fynn.smsforwarder.model.bean;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author Fynn
 */
public class UserAuthenticator extends Authenticator {

    private String username;
    private String password;

    public UserAuthenticator() {
    }

    public UserAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}