package com.fynn.smsforwarder.business.sms;

import android.util.Pair;

import com.fynn.smsforwarder.common.SmsExtractor;
import com.fynn.smsforwarder.model.bean.Sms;

import org.fynn.appu.cache.LruCache;

/**
 * @author Fynn
 * @date 18/2/14
 */
public class AuthCodeCache {

    public static final int CACHE_SIZE = 100;
    private final static LruCache<Long, Pair> CACHE = new LruCache<Long, Pair>() {
        // no-op
    };
    private static final Object LOCK = new Object();
    private static AuthCodeCache authCodeCache;

    private AuthCodeCache() {
        CACHE.setupCapacity(CACHE_SIZE);
    }

    public static AuthCodeCache get() {
        synchronized (LOCK) {
            if (authCodeCache == null) {
                synchronized (LOCK) {
                    authCodeCache = new AuthCodeCache();
                }
            }
        }
        return authCodeCache;
    }

    public Pair fetchCode(Sms sms) {
        if (sms == null) {
            return null;
        }

        Pair p = CACHE.get(sms.id);

        if (p != null) {
            return p;
        }

        Pair code = SmsExtractor.extractCaptcha(sms.msg);

        if (code == null) {
            code = Pair.create("", "");
        }

        synchronized (LOCK) {
            CACHE.put(sms.id, code);
        }
        return code;
    }
}
