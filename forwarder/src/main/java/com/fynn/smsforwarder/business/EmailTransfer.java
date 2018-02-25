package com.fynn.smsforwarder.business;

import com.fynn.smsforwarder.common.db.SPs;
import com.fynn.smsforwarder.model.bean.Email;
import com.fynn.smsforwarder.model.bean.UserAuthenticator;

import org.fynn.appu.util.CharsUtils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * @author Fynn
 * @date 18/2/14
 */
public class EmailTransfer {

    /**
     * 发送邮件
     *
     * @param email
     * @throws Exception
     */
    public static void send(Email email) throws Exception {
        // 判断是否需要身份认证
        UserAuthenticator authenticator = null;
        Properties pro = email.getProperties();

        // 如果需要身份认证，则创建一个密码验证器
        if (email.validate) {
            authenticator = new UserAuthenticator(email.username, email.password);
        }

        // 根据邮件会话属性和密码验证器构造一个发送邮件的 session
        Session session = Session.getDefaultInstance(pro, authenticator);

        // 根据 session 创建一个邮件消息
        Message message = new MimeMessage(session);

        // 创建邮件发送者地址
        String address = email.fromAddress;
        String nickname = email.nickname;

        if (!CharsUtils.isEmptyAfterTrimming(nickname)) {
            nickname = MimeUtility.encodeText(nickname);
            address = nickname + " <" + address + ">";
        }

        Address from = new InternetAddress(address);

        // 设置邮件消息的发送者
        message.setFrom(from);

        // 创建邮件的接收者地址，并设置到邮件消息中
        Address to = new InternetAddress(email.toAddress);

        // Message.RecipientType.TO 属性表示接收者的类型为 TO
        message.setRecipient(Message.RecipientType.TO, to);

        // 设置邮件消息的主题
        message.setSubject(email.subject);

        // 设置邮件消息发送的时间
        message.setSentDate(new Date());

        // MiniMultipart类是一个容器类，包含 MimeBodyPart 类型的对象
        Multipart mainPart = new MimeMultipart();

        // 创建一个包含 HTML 内容的 MimeBodyPart
        BodyPart html = new MimeBodyPart();

        // 设置邮件正文内容，使其支持html标签
        html.setContent(email.content, "text/html; charset=utf-8");
        mainPart.addBodyPart(html);

        // 将 MiniMultipart 对象设置为邮件内容
        message.setContent(mainPart);

        // 发送邮件
        Transport.send(message);
    }

    /**
     * 根据用户设置，生成 Email 对象
     *
     * @param subject
     * @param content
     * @param nickname
     * @return
     */
    public static Email genEmailData(String subject, String content, String nickname) {
        String serverHost = SPs.getServerHost();
        String serverPort = SPs.getServerPort();
        String username = SPs.getUsername();
        String email = SPs.getEmail();
        boolean ssl = SPs.isEnabledSSL();
        String password = SPs.getPassword();

        Email data = new Email();
        data.enabledSSL = ssl;
        data.serverHost = serverHost;
        data.serverPort = serverPort;
        data.username = username;
        data.toAddress = email;
        data.password = password;
        data.subject = subject;
        data.content = content;
        data.nickname = nickname;
        data.fromAddress = username;
        data.validate = true;

        if (EmailTransfer.check(data)) {
            return data;
        }

        return null;
    }

    /**
     * check email
     *
     * @param email
     * @return
     */
    private static boolean check(Email email) {
        if (CharsUtils.isEmptyAfterTrimming(email.serverHost)) {
            return false;
        }

        if (CharsUtils.isEmptyAfterTrimming(email.serverPort)) {
            return false;
        }

        if (CharsUtils.isEmptyAfterTrimming(email.username)) {
            return false;
        }

        if (CharsUtils.isEmptyAfterTrimming(email.password)) {
            return false;
        }

        if (CharsUtils.isEmptyAfterTrimming(email.toAddress)) {
            return false;
        }

        return true;
    }
}
