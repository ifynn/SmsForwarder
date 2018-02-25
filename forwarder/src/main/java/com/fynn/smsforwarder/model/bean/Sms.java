package com.fynn.smsforwarder.model.bean;

/**
 * Created by Fynn on 18/2/6.
 */

public class Sms {

    /**
     * 发件人
     */
    public String address;

    /**
     * 消息内容
     */
    public String msg;

    /**
     * 发件日期
     */
    public long date;

    /**
     * 消息 id
     */
    public long id;

    @Override
    public String toString() {
        return "Sms{" +
                "address='" + address + '\'' +
                ", msg='" + msg + '\'' +
                ", date=" + date +
                ", id=" + id +
                '}';
    }
}
