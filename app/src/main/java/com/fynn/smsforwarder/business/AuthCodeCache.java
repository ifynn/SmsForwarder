package com.fynn.smsforwarder.business;

import com.fynn.smsforwarder.common.SmsExtractor;
import com.fynn.smsforwarder.model.bean.Sms;

import org.fynn.appu.cache.LruCache;
import org.fynn.appu.common.Immutable;

/**
 * @author Fynn
 * @date 18/2/14
 */
public class AuthCodeCache extends Immutable {

    public static final int CACHE_SIZE = 100;
    private static Object LOCKED = new Object();

    private static AuthCodeCache authCodeCache;

    private final static LruCache<Long, String> cache = new LruCache<Long, String>() {
        // no-op
    };

    private AuthCodeCache() {
        cache.setupCapacity(CACHE_SIZE);
    }

    public static AuthCodeCache get() {
        synchronized (LOCKED) {
            if (authCodeCache == null) {
                synchronized (LOCKED) {
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
