package com.fynn.smsforwarder.business;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.fynn.smsforwarder.model.bean.InboxSms;
import com.fynn.smsforwarder.model.bean.SmsReceiver;

import org.fynn.appu.AppU;

/**
 * 兼容酷派系列手机（包括 360）
 *
 * @author Fynn
 * @date 2018/6/1
 */
public class QihooReceiverInquirer implements SmsReceiverInquirer<InboxSms> {

    private static final String CONTENT_ITEM_INFO = "content://mms-sms/itemInfo";
    private static final String CONTENT_SIM_INFO = "content://telephony/siminfo";

    private static final String COLUMN_SLOT_INDEX = "network_type";
    private static final String COLUMN_SIM_ID = "sim_id";
    private static final String COLUMN_DISPLAY_NAME = "display_name";
    private static final String COLUMN_NUMBER = "number";

    @Override
    public SmsReceiver getReceiver(InboxSms sms) {
        long id = sms.itemInfoId;
        ContentResolver resolver = AppU.app().getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(CONTENT_ITEM_INFO),
                new String[]{COLUMN_SLOT_INDEX}, "_id = ?",
                new String[]{String.valueOf(id)}, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.getCount() <= 0 || !cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        int slot = cursor.getInt(cursor.getColumnIndex(COLUMN_SLOT_INDEX));
        cursor.close();

        slot = slot == 1 ? 0 : 1;

        cursor = resolver.query(Uri.parse(CONTENT_SIM_INFO),
                null, COLUMN_SIM_ID + " = ?",
                new String[]{String.valueOf(slot)}, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.getCount() <= 0 || !cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        SmsReceiver r = new SmsReceiver();
        r.cardSlot = slot;
        r.number = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER));
        r.operator = cursor.getString(cursor.getColumnIndex(COLUMN_DISPLAY_NAME));

        return r;
    }
}
