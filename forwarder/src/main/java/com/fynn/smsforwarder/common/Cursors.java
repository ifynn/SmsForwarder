package com.fynn.smsforwarder.common;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import org.fynn.appu.AppU;
import org.fynn.appu.util.LogU;

/**
 * @author lifs
 * @date 2018/5/31
 */
public class Cursors {

    /**
     * 遍历 Cursor
     *
     * @param c
     * @return
     */
    public static StringBuilder parseCursor(Cursor c) {
        StringBuilder builder = new StringBuilder();
        if (c == null) {
            return builder;
        }

        int count = c.getColumnCount();

        while (c.moveToNext()) {
            for (int i = 0; i < count; i++) {
                String name = c.getColumnName(i);
                String value = c.getString(i);
                builder.append(name).append(": ").append(value).append(", ");
            }

            if (builder.toString().lastIndexOf(", ") >= 0) {
                builder.delete(builder.length() - 2, builder.length());
            }

            builder.append("\n");
        }

        c.close();
        return builder;
    }

    public static void parseSms() {
        ContentResolver resolver = AppU.app().getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(SmsManager.CONTENT_SMS_INBOX),
                null, null, null, "_id desc");
        LogU.e("sms", parseCursor(cursor));
    }

    public static void parseSimInfo() {
        ContentResolver resolver = AppU.app().getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://telephony/siminfo"),
                null, null, null, "_id desc");
        LogU.e("smsinfo", parseCursor(cursor));
    }
}
