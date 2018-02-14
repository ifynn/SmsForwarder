package com.fynn.smsforwarder.common;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.util.Pair;

import org.fynn.appu.AppU;
import org.fynn.appu.util.CharsUtils;

/**
 * @author Fynn
 * @date 18/2/13
 */
public final class SmsManager {

    public static final String CONTENT_SMS = "content://sms/";
    public static final String CONTENT_SMS_INBOX = CONTENT_SMS + "inbox";

    private static final Pair<Character, Character>[] CHARS = new Pair[]{
            Pair.create('【', '】'),
            Pair.create('[', ']')
    };

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
        for (Pair<Character, Character> p : CHARS) {
            String sender = fetchSmsSender(msg, p.first, p.second);

            if (!CharsUtils.isEmptyAfterTrimming(sender)) {
                return sender;
            }
        }
        return null;
    }

    public static final String fetchSmsSender(String msg, char startChar, char endChar) {
        if (msg == null) {
            return null;
        }

        int start = msg.indexOf(startChar);
        int end = msg.lastIndexOf(endChar);

        if (start < 0 || end < 0) {
            return null;
        }

        if (end - start < 1) {
            return null;
        }

        if (start > msg.length() - 1 || end > msg.length() - 1) {
            return null;
        }

        return msg.substring(start + 1, end);
    }
}
