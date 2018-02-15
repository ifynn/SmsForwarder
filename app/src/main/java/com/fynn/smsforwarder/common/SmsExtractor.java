package com.fynn.smsforwarder.common;

import android.text.TextUtils;

import org.fynn.appu.util.CharsUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fynn
 */
public class SmsExtractor {

    private static final String SMS_REGEX = "[0-9a-zA-Z]{6}";

    private static final String[] CODES = {"验证码", "校验码"};

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

        boolean contains = false;

        for (String code : CODES) {
            if (msg.contains(code)) {
                contains = true;
                break;
            }
        }

        if (!contains) {
            return null;
        }

        Matcher m = Patterns.p.matcher(msg);
        int count = 0;
        String code = null;

        while (m.find()) {
            String s = m.group();

            if (CharsUtils.isEmptyAfterTrimming(s)) {
                continue;
            }

            count++;

            if (count > 1) {
                return null;
            } else {
                code = s;
            }
        }

        return code;
    }

    static class Patterns {
        public static final Pattern p = Pattern.compile(SMS_REGEX);
    }
}