package com.fynn.smsforwarder.common.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;

import com.fynn.smsforwarder.business.QihooSmsFetcher;
import com.fynn.smsforwarder.business.SmsCache;
import com.fynn.smsforwarder.business.SmsFetcher;
import com.fynn.smsforwarder.model.bean.InboxSms;
import com.fynn.smsforwarder.model.bean.Sms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lifs
 * @date 18/2/13
 */
public final class Dbs {

    private static final SmsFetcher FETCHER;

    static {
        if (Build.BRAND.equals("360")) {
            FETCHER = new QihooSmsFetcher();
        } else {
            FETCHER = new SmsFetcher() {
                @Override
                public InboxSms fetchRecentOneInboxSms() {
                    return null;
                }
            };
        }
    }

    /**
     * 获取收件箱中最新的一条短信
     *
     * @return
     */
    public static synchronized InboxSms fetchRecentOneInboxSms() {
        return FETCHER.fetchRecentOneInboxSms();
    }

    /**
     * 向数据库中插入一条短信数据
     *
     * @param sms
     * @return
     */
    public static long insertSms(InboxSms sms) {
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

    public static List readSmsList(Cursor cursor) {
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

    public static InboxSms readSms(Cursor cursor) {
        List<InboxSms> smsList = readSmsList(cursor);

        if (smsList.size() > 0) {
            return smsList.get(0);
        }

        return null;
    }

    /**
     * 分页查询数据库中短信
     *
     * @param offset
     * @param limit
     * @return
     */
    public static List<Sms> readSmsPage(int offset, int limit) {
        Cursor c = SmsDbHelper.get().queryIdPage(offset, limit);

        if (c == null) {
            return Collections.emptyList();
        }

        List<Sms> smsList = new ArrayList<>(limit);

        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex(SmsDbHelper.ID));
            Sms sms = SmsCache.get().getSms(id);
            smsList.add(sms);
        }

        return smsList;
    }
}
