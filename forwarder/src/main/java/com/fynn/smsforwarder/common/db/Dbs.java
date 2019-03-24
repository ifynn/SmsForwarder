package com.fynn.smsforwarder.common.db;

import android.database.Cursor;
import android.os.Build;

import com.fynn.smsforwarder.business.sms.LollipopSmsFetcher;
import com.fynn.smsforwarder.business.sms.QihooSmsFetcher;
import com.fynn.smsforwarder.business.sms.SmsFetcher;
import com.fynn.smsforwarder.business.sms.SmsFetcherFactory;
import com.fynn.smsforwarder.model.bean.InboxSms;

import java.util.Collections;
import java.util.List;

/**
 * @author Fynn
 * @date 18/2/13
 */
public final class Dbs {

    private static final SmsFetcher FETCHER;

    static {
        FETCHER = new Factory().create();
    }

    /**
     * 获取收件箱中最新的一条短信
     *
     * @return
     */
    public static synchronized InboxSms fetchRecentOneInboxSms() {
        return FETCHER.fetchRecentOneInboxSms();
    }

    /**
     * 向数据库中插入一条短信数据
     *
     * @param sms
     * @return
     */
    public static long insertSms(InboxSms sms) {
        return FETCHER.insertSms(sms);
    }

    public static List<InboxSms> readSms(Cursor cursor) {
        return FETCHER.readSms(cursor);
    }

    /**
     * 分页查询数据库中短信
     *
     * @param offset
     * @param limit
     * @return
     */
    public static List<InboxSms> readSmsPage(int offset, int limit) {
        return FETCHER.readSmsPage(offset, limit);
    }

    static class Factory implements SmsFetcherFactory {

        private static final String BRAND_QIHOO = "360";

        @Override
        public SmsFetcher create() {
            if (Build.BRAND.equals(BRAND_QIHOO)) {
                return new QihooSmsFetcher();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                return new LollipopSmsFetcher();
            }

            return new SmsFetcher() {

                @Override
                public InboxSms fetchRecentOneInboxSms() {
                    return null;
                }

                @Override
                public long insertSms(InboxSms sms) {
                    return 0;
                }

                @Override
                public List readSms(Cursor cursor) {
                    return Collections.emptyList();
                }

                @Override
                public List<InboxSms> readSmsPage(int offset, int limit) {
                    return Collections.emptyList();
                }
            };
        }
    }
}
