package com.fynn.smsforwarder.model.bean;

/**
 * @author Fynn
 * @date 2018/6/1
 */
public class SmsReceiver {

    /**
     * 卡槽值，从 0 开始
     */
    public int cardSlot;

    /**
     * 接收 sms 的号码
     */
    public String number;

    /**
     * 运营商
     */
    public String operator;
}
