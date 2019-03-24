package com.fynn.smsforwarder.business.sms;

/**
 * @author lifs
 * @date 2018/6/4
 */
public interface SmsFetcherFactory {

    /**
     * 创建 SmsFetcher
     *
     * @return
     */
    SmsFetcher create();
}
