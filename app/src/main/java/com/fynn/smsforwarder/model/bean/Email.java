package com.fynn.smsforwarder.model.bean;

import java.util.Properties;

/**
 * @author Fynn
 * @date 18/2/14
 */
public class Email {

    /**
     * 是否进行身份验证
     */
    public boolean validate;

    /**
     * 登录邮件发送服务器的用户名
     */
    public String username;

    /**
     * 登录邮件发送服务器的密码
     */
    public String password;

    /**
     * 邮件发送者地址
     */
    public String fromAddress;

    /**
     * 邮件接收者地址
     */
    public String toAddress;

    /**
     * 邮件发送者昵称
     */
    public String nickname;

    /**
     * 邮件主题
     */
    public String subject;

    /**
     * 邮件内容
     */
    public String content;

    /**
     * 是否开启 SSL
     */
    public boolean enabledSSL;

    /**
     * 发送邮件的服务器地址
     */
    public String serverHost;

    /**
     * 发送邮件的服务器端口
     */
    public String serverPort;

    /**
     * 获得邮件会话属性
     */
    public Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", serverHost);
        p.put("mail.smtp.port", serverPort);
        p.put("mail.smtp.auth", validate ? "true" : "false");

        if (enabledSSL) {
            p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            p.setProperty("mail.smtp.socketFactory.fallback", "false");
            p.setProperty("mail.smtp.socketFactory.port", serverPort);
        }

        return p;
    }
}
