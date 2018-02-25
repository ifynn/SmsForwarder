package com.fynn.smsforwarder.model.consts;

import com.fynn.smsforwarder.common.db.SmsDbHelper;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Fynn
 * @date 18/2/13
 */
public final class Consts {

    /**
     * 公钥
     */
    public static final String PUB_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAL8Zpd/ApPhT6J4ilm1lHtwEGfrC5W2HAR6tg8qcbMLSwQKJncMoXffXjvll0JJov3eBJmy4UNDoEUnzc/FPDnsCAwEAAQ==";

    public static class EmailConst {
        public static final String SERVER_HOST = "mail.host";
        public static final String SERVER_PORT = "mail.port";
        public static final String PASSWORD = "mail.psw";
        public static final String USERNAME = "mail.username";
        public static final String SSL = "mail.ssl";
        public static final String EMAIL = "mail.email";
        public static final String BATTERY_NOTIFY = "battery.notify";
    }

    public static AtomicLong sSmsCount = new AtomicLong(SmsDbHelper.get().getCount());

}
