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

    /**
     * 接收者
     */
    public String receiver;

    /**
     * 卡槽值
     */
    public int slot;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Sms{");
        sb.append("address='").append(address).append('\'');
        sb.append(", msg='").append(msg).append('\'');
        sb.append(", date=").append(date);
        sb.append(", id=").append(id);
        sb.append(", receiver='").append(receiver).append('\'');
        sb.append(", slot=").append(slot);
        sb.append('}');
        return sb.toString();
    }
}
