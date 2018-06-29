package com.fynn.smsforwarder.model.bean;

/**
 * Created by fynn on 2018/2/13.
 */

public class InboxSms extends Sms {

    public int type;

    public boolean read;

    /**
     * 酷派系列手机特有的字段
     */
    public long itemInfoId;
}
