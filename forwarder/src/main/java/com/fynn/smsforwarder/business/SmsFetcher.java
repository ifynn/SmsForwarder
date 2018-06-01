package com.fynn.smsforwarder.business;

import com.fynn.smsforwarder.model.bean.InboxSms;

/**
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
}
