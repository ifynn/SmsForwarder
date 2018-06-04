package com.fynn.smsforwarder.business.sms;

import android.database.Cursor;

import com.fynn.smsforwarder.model.bean.InboxSms;

import java.util.List;

/**
 * 从不同型号手机数据库中处理短信
 *
 * @author Fynn
 * @date 2018/6/1
 */
public interface SmsFetcher {

    /**
     * 获取收件箱里的最新一条短信
     *
     * @return
     */
    InboxSms fetchRecentOneInboxSms();

    /**
     * 向数据库中插入一条数据
     *
     * @param sms
     * @return
     */
    long insertSms(InboxSms sms);

    /**
     * 获取 Sms
     *
     * @param cursor
     * @return
     */
    List<InboxSms> readSms(Cursor cursor);

    /**
     * 分页查询短信记录
     *
     * @param offset 偏移位置
     * @param limit 每页数量
     * @return
     */

    List<InboxSms> readSmsPage(int offset, int limit);
}
