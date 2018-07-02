package com.fynn.smsforwarder.business.sms;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.fynn.smsforwarder.model.bean.InboxSms;
import com.fynn.smsforwarder.model.bean.SmsReceiver;
import com.fynn.smsforwarder.model.consts.Sim;

import org.fynn.appu.AppU;

/**
 * @author lifs
 * @date 2018/7/2
 */
public class LollipopSmsInquirer implements SmsReceiverInquirer<InboxSms> {

    @Override
    public SmsReceiver getReceiver(InboxSms sms) {
        long id = sms.simId;

        ContentResolver resolver = AppU.app().getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(Sim.CONTENT_SIM_INFO),
                null, Sim.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.getCount() <= 0 || !cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        SmsReceiver r = new SmsReceiver();
        r.cardSlot = (int) (id + 1);
        r.number = cursor.getString(cursor.getColumnIndex(Sim.COLUMN_NUMBER));
        r.operator = cursor.getString(cursor.getColumnIndex(Sim.COLUMN_DISPLAY_NAME));

        return r;
    }
}
