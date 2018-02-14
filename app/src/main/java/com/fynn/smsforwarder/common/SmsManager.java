package com.fynn.smsforwarder.common;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;

import org.fynn.appu.AppU;

/**
 * @author Fynn
 * @date 18/2/13
 */
public final class SmsManager {

    public static final String CONTENT_SMS = "content://sms/";
    public static final String CONTENT_SMS_INBOX = CONTENT_SMS + "inbox";

    /**
     * 注册短信收发观察者
     *
     * @param observer
     */
    public static final void registerContentObserver(ContentObserver observer) {
        ContentResolver resolver = AppU.app().getContentResolver();
        resolver.registerContentObserver(Uri.parse(CONTENT_SMS), true, observer);
    }

    /**
     * 取消注册短信收发观察者
     *
     * @param observer
     */
    public static final void unregisterContentObserver(ContentObserver observer) {
        ContentResolver resolver = AppU.app().getContentResolver();
        resolver.unregisterContentObserver(observer);
    }

    /**
     * 获取短信发送者
     *
     * @return
     */
    public static final String fetchSmsSender(String msg) {
        if (msg == null) {
            return "";
        }

        int start = msg.indexOf('【');
        int end = msg.lastIndexOf('】');

        if (start < 0 || end < 0) {
            return "";
        }

        if (end - start < 1) {
            return "";
        }

        if (start > msg.length() - 1 || end > msg.length() - 1) {
            return "";
        }

        return msg.substring(start + 1, end);
    }
}
