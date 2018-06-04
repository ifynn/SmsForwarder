package com.fynn.smsforwarder.common;

import android.os.Build;

import com.fynn.smsforwarder.business.sms.QihooReceiverInquirer;
import com.fynn.smsforwarder.business.sms.SmsReceiverInquirer;
import com.fynn.smsforwarder.model.bean.Sms;
import com.fynn.smsforwarder.model.bean.SmsReceiver;

/**
 * @author Fynn
 * @date 2018/6/1
 */
public final class SmsReceiverManager {

    private final static SmsReceiverInquirer IMPL;

    static {
        if (Build.BRAND.equals("360")) {
            IMPL = new QihooReceiverInquirer();
        } else {
            IMPL = new ExtraInquirer();
        }

    }

    public static SmsReceiver getSmsReceiver(Sms sms) {
        return IMPL.getReceiver(sms);
    }

    static class ExtraInquirer implements SmsReceiverInquirer<Sms> {

        @Override
        public SmsReceiver getReceiver(Sms sms) {
            return null;
        }
    }
}
