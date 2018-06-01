package com.fynn.smsforwarder.business;

import com.fynn.smsforwarder.model.bean.Sms;
import com.fynn.smsforwarder.model.bean.SmsReceiver;

/**
 * @author Fynn
 * @date 2018/6/1
 */
public interface SmsReceiverInquirer<S extends Sms> {

    /**
     * 获取短信接收者
     *
     * @param sms
     * @return
     */
    SmsReceiver getReceiver(S sms);
}