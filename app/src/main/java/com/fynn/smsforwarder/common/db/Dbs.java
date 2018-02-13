package com.fynn.smsforwarder.common.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.fynn.smsforwarder.common.SmsManager;
import com.fynn.smsforwarder.model.bean.Sms;

import org.fynn.appu.AppU;

/**
 * @author lifs
 * @date 18/2/13
 */
public final class Dbs {

    // TODO: 18/2/13 LRU 缓存数据库短信

    /**
     * 获取收件箱中最新的一条短信
     *
     * @return
     */
    public static final synchronized Sms fetchRecentOneInboxSms() {
        ContentResolver resolver = AppU.app().getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(SmsManager.CONTENT_SMS_INBOX),
                null, null, null, "_id desc");

        if (cursor == null) {
            return null;
        }

        if (cursor.getCount() <= 0 || !cursor.moveToFirst()) {
            return null;
        }

        String address = cursor.getString(cursor.getColumnIndex("address"));
        String body = cursor.getString(cursor.getColumnIndex("body"));
        long date = cursor.getLong(cursor.getColumnIndex("date"));
        int id = cursor.getInt(cursor.getColumnIndex("_id"));

        Sms sms = new Sms();
        sms.address = address;
        sms.date = date;
        sms.id = id;
        sms.msg = body;

        cursor.close();
        return sms;
    }

    /**
     * 向数据库中插入一条短信数据
     *
     * @param sms
     * @return
     */
    public static long insert(Sms sms) {
        if (sms == null) {
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(SmsDbHelper.ID, sms.id);
        values.put(SmsDbHelper.ADDRESS, sms.address);
        values.put(SmsDbHelper.BODY, sms.msg);
        values.put(SmsDbHelper.DATE, sms.date);

        synchronized (Dbs.class) {
            return SmsDbHelper.get().insert(values);
        }
    }
}
