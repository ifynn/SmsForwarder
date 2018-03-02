package com.fynn.smsforwarder.common;

import android.util.Pair;

import org.fynn.appu.util.CharsUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fynn
 */
public class SmsExtractor {

    private static final String SMS_REGEX = "[\\da-zA-Z]{6}|[\\d]{4}";

    private static final String[] CODES = {"验证码", "校验码", "随机码"};

    /**
     * 解析短信中的验证码
     *
     * @param msg
     * @return
     */
    public static Pair<String, String> extractCaptcha(String msg) {
        if (CharsUtils.isEmptyAfterTrimming(msg)) {
            return null;
        }

        String matching = null;

        for (String code : CODES) {
            if (msg.contains(code)) {
                matching = code;
                break;
            }
        }

        if (CharsUtils.isEmpty(matching)) {
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

        return Pair.create(matching, code);
    }

    static class Patterns {
        public static final Pattern p = Pattern.compile(SMS_REGEX);
    }
}