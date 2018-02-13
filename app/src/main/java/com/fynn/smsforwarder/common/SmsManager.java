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
}
