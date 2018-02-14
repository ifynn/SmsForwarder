package com.fynn.smsforwarder.common;

import android.text.TextUtils;

import org.fynn.appu.util.CharsUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fynn
 */
public class SmsExtractor {

    private static final String SMS_REGEX = "[0-9a-zA-Z]{4,8}";

    private static final String CODE = "码";

    /**
     * 解析短信中的验证码
     *
     * @param msg
     * @return
     */
    public static String extractCaptcha(String msg) {
        if (CharsUtils.isEmptyAfterTrimming(msg)) {
           return null;
        }

        if (!msg.contains(CODE)) {
            return null;
        }

        Matcher m = Patterns.p.matcher(msg);

        while (m.find()) {
            String s = m.group();
            
            if (CharsUtils.isEmptyAfterTrimming(s)) {
                continue;
            }
            
            if (TextUtils.isDigitsOnly(s) && s.length() == 6) {
                return s;
            }
            
            if (s.length() == 6) {
                return s;
            }
            
            return s;
        }

        return null;
    }

    static class Patterns {
        public static final Pattern p = Pattern.compile(SMS_REGEX);
    }
}