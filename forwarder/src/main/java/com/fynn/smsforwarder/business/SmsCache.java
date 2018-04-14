package com.fynn.smsforwarder.business;

import com.fynn.smsforwarder.common.db.Dbs;
import com.fynn.smsforwarder.common.db.SmsDbHelper;
import com.fynn.smsforwarder.model.bean.Sms;

import org.fynn.appu.cache.LruCache;

/**
 * @author lifs
 * @date 18/3/2
 */
public class SmsCache {

    public static final int CACHE_SIZE = 100;
    private static final Object LOCK = new Object();

    private final static LruCache<Long, Sms> cache = new LruCache<Long, Sms>() {
        // no-op
    };

    private static SmsCache smsCache;

    private SmsCache() {
        cache.setupCapacity(CACHE_SIZE);
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

    public Sms getSms(long id) {
        Sms s = cache.get(id);

        if (s != null) {
            return s;
        }

        s = Dbs.readSms(SmsDbHelper.get().query(id));

        if (s != null) {
            synchronized (LOCK) {
                cache.put(id, s);
            }
        }

        return s;
    }
}
