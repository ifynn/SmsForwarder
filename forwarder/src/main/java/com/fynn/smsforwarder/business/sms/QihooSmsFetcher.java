package com.fynn.smsforwarder.business.sms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.fynn.smsforwarder.business.perimission.Permissions;
import com.fynn.smsforwarder.common.SmsManager;
import com.fynn.smsforwarder.common.db.Dbs;
import com.fynn.smsforwarder.common.db.SmsDbHelper;
import com.fynn.smsforwarder.model.bean.InboxSms;

import org.fynn.appu.AppU;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Fynn
 * @date 2018/6/1
 */
public class QihooSmsFetcher implements SmsFetcher {

    @Override
    public InboxSms fetchRecentOneInboxSms() {
        if (!Permissions.hasPermission(Manifest.permission.READ_SMS)) {
            return null;
        }

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

    @Override
    public long insertSms(InboxSms sms) {
        if (sms == null) {
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(SmsDbHelper.ID, sms.id);
        values.put(SmsDbHelper.ADDRESS, sms.address);
        values.put(SmsDbHelper.BODY, sms.msg);
        values.put(SmsDbHelper.DATE, sms.date);
        values.put(SmsDbHelper.RECEIVER, sms.receiver);
        values.put(SmsDbHelper.ITEM_INFO_ID, sms.itemInfoId);

        synchronized (Dbs.class) {
            return SmsDbHelper.get().insert(values);
        }
    }

    @Override
    public List<InboxSms> readSms(Cursor cursor) {
        ArrayList<InboxSms> smses = new ArrayList<InboxSms>();

        if (cursor == null) {
            return smses;
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(SmsDbHelper.ID));
            String from = cursor.getString(cursor.getColumnIndex(SmsDbHelper.ADDRESS));
            String content = cursor.getString(cursor.getColumnIndex(SmsDbHelper.BODY));
            long date = cursor.getLong(cursor.getColumnIndex(SmsDbHelper.DATE));
            String number = cursor.getString(cursor.getColumnIndex(SmsDbHelper.RECEIVER));
            long itemInfoId = cursor.getLong(cursor.getColumnIndex(SmsDbHelper.ITEM_INFO_ID));

            InboxSms sms = new InboxSms();
            sms.address = from;
            sms.date = date;
            sms.id = id;
            sms.msg = content;
            sms.receiver = number;
            sms.itemInfoId = itemInfoId;

            smses.add(sms);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return smses;
    }

    @Override
    public List<InboxSms> readSmsPage(int offset, int limit) {
        Cursor c = SmsDbHelper.get().queryIdPage(offset, limit);

        if (c == null) {
            return Collections.emptyList();
        }

        List<InboxSms> smsList = new ArrayList<>(limit);

        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex(SmsDbHelper.ID));
            InboxSms sms = SmsCache.get().getSms(id);
            smsList.add(sms);
        }

        return smsList;
    }
}
