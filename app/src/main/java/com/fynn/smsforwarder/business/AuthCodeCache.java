package com.fynn.smsforwarder.business;

import com.fynn.smsforwarder.common.SmsExtractor;
import com.fynn.smsforwarder.model.bean.Sms;

import org.fynn.appu.cache.LruCache;

/**
 * @author Fynn
 * @date 18/2/14
 */
public class AuthCodeCache {

    public static final int CACHE_SIZE = 100;
    private final static LruCache<Long, String> cache = new LruCache<Long, String>() {
        // no-op
    };
    private static Object LOCK = new Object();
    private static AuthCodeCache authCodeCache;

    private AuthCodeCache() {
        cache.setupCapacity(CACHE_SIZE);
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

    public String fetchCode(Sms sms) {
        if (sms == null) {
            return "";
        }

        String s = cache.get(sms.id);

        if (s != null) {
            return s;
        }

        String code = SmsExtractor.extractCaptcha(sms.msg);

        cache.put(sms.id, code == null ? "" : code);
        return code;
    }
}
