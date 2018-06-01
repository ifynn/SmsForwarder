package com.fynn.smsforwarder.business;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.fynn.smsforwarder.common.SmsManager;
import com.fynn.smsforwarder.model.bean.InboxSms;

import org.fynn.appu.AppU;

/**
 * @author Fynn
 * @date 2018/6/1
 */
public class QihooSmsFetcher implements SmsFetcher {

    @Override
    public InboxSms fetchRecentOneInboxSms() {
        ContentResolver resolver = AppU.app().getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(SmsManager.CONTENT_SMS_INBOX),
                null, null, null, "_id desc");

        if (cursor == null) {
            return null;
        }

        if (cursor.getCount() <= 0 || !cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        String address = cursor.getString(cursor.getColumnIndex("address"));
        String body = cursor.getString(cursor.getColumnIndex("body"));
        long date = cursor.getLong(cursor.getColumnIndex("date"));
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        int read = cursor.getInt(cursor.getColumnIndex("read"));
        int type = cursor.getInt(cursor.getColumnIndex("type"));
        long itemInfoId = cursor.getLong(cursor.getColumnIndex("itemInfoid"));

        InboxSms sms = new InboxSms();
        sms.address = address;
        sms.date = date;
        sms.id = id;
        sms.msg = body;
        sms.read = read == 1 ? true : false;
        sms.type = type;
        sms.itemInfoId = itemInfoId;

        cursor.close();
        return sms;
    }
}
