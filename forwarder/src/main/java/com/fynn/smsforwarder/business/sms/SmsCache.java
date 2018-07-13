package com.fynn.smsforwarder.business.sms;

import com.fynn.smsforwarder.common.db.Dbs;
import com.fynn.smsforwarder.common.db.SmsDbHelper;
import com.fynn.smsforwarder.model.bean.InboxSms;

import org.fynn.appu.cache.LruCache;

import java.util.List;

/**
 * @author lifs
 * @date 18/3/2
 */
public class SmsCache {

    public static final int CACHE_SIZE = 100;
    private static final Object LOCK = new Object();

    private final static LruCache<Long, InboxSms> CACHE = new LruCache<Long, InboxSms>() {
        // no-op
    };

    private static SmsCache smsCache;

    private SmsCache() {
        CACHE.setupCapacity(CACHE_SIZE);
    }

    public static SmsCache get() {
        synchronized (LOCK) {
            if (smsCache == null) {
                synchronized (LOCK) {
                    smsCache = new SmsCache();
                }
            }
        }
        return smsCache;
    }

    public InboxSms getSms(long id) {
        InboxSms s = CACHE.get(id);

        if (s != null) {
            return s;
        }

        List<InboxSms> smses = Dbs.readSms(SmsDbHelper.get().query(id));

        if (smses != null && smses.size() > 0) {
            s = smses.get(0);
        }

        synchronized (LOCK) {
            CACHE.put(id, s);
        }

        return s;
    }
}
