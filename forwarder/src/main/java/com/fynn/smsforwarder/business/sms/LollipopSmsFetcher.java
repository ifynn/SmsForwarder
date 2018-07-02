package com.fynn.smsforwarder.business.sms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.fynn.smsforwarder.business.perimission.Permissions;
import com.fynn.smsforwarder.common.CloseUtils;
import com.fynn.smsforwarder.common.SmsManager;
import com.fynn.smsforwarder.common.db.Dbs;
import com.fynn.smsforwarder.common.db.SmsDbHelper;
import com.fynn.smsforwarder.model.bean.InboxSms;

import org.fynn.appu.AppU;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Android 5.0 以上处理短信相关
 *
 * @author lifs
 * @date 2018/7/2
 */
public class LollipopSmsFetcher implements SmsFetcher {

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
            CloseUtils.close(cursor);
            return null;
        }

        String address = "";
        String body = "";
        long date = 0;
        int id = -1;
        int read = 0;
        int type = 1;
        long simId = -1;

        try {
            address = cursor.getString(cursor.getColumnIndex("address"));
            body = cursor.getString(cursor.getColumnIndex("body"));
            date = cursor.getLong(cursor.getColumnIndex("date"));
            id = cursor.getInt(cursor.getColumnIndex("_id"));
            read = cursor.getInt(cursor.getColumnIndex("read"));
            type = cursor.getInt(cursor.getColumnIndex("type"));
            simId = cursor.getLong(cursor.getColumnIndex("sub_id"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        InboxSms sms = new InboxSms();
        sms.address = address;
        sms.date = date;
        sms.id = id;
        sms.msg = body;
        sms.read = read == 1 ? true : false;
        sms.type = type;
        sms.simId = simId;

        CloseUtils.close(cursor);
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
        values.put(SmsDbHelper.SIM_ID, sms.simId);

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
            long simId = cursor.getLong(cursor.getColumnIndex(SmsDbHelper.SIM_ID));

            InboxSms sms = new InboxSms();
            sms.address = from;
            sms.date = date;
            sms.id = id;
            sms.msg = content;
            sms.receiver = number;
            sms.simId = simId;

            smses.add(sms);
        }

        if (!cursor.isClosed()) {
            CloseUtils.close(cursor);
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
