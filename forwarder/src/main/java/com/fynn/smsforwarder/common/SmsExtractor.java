package com.fynn.smsforwarder.common;

import android.support.v4.util.ArraySet;
import android.text.TextUtils;
import android.util.Pair;

import org.fynn.appu.util.CharsUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fynn
 */
public class SmsExtractor {

    private static final String SMS_REGEX = "[\\da-zA-Z]{6}|[\\d]{4}";

    private static final String[] CODES = {"验证码", "校验码", "随机码", "安全码"};

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

        ArraySet<String> sixDigits = new ArraySet<>(1);
        ArraySet<String> dgt$Letter = new ArraySet<>(1);
        ArraySet<String> fourDigits = new ArraySet<>(1);

        while (m.find()) {
            String s = m.group();

            if (CharsUtils.isEmptyAfterTrimming(s)) {
                continue;
            }

            if (TextUtils.isDigitsOnly(s)) {
                if (s.length() == 6) {
                    sixDigits.add(s);
                } else {
                    fourDigits.add(s);
                }
            } else {
                dgt$Letter.add(s);
            }
        }

        String code = findCode(sixDigits, dgt$Letter, fourDigits);

        if (CharsUtils.isEmpty(code)) {
            return null;
        }

        return Pair.create(matching, code);
    }

    private static String findCode(ArraySet<String>... codes) {
        if (codes[0].size() == 1) {
            return codes[0].valueAt(0);
        }

        if (codes[0].size() > 1) {
            return null;
        }

        if (codes[1].size() == 1) {
            return codes[1].valueAt(0);
        }

        if (codes[1].size() > 1) {
            return null;
        }

        if (codes[2].size() == 1) {
            return codes[2].valueAt(0);
        }

        return null;
    }

    static class Patterns {
        public static final Pattern p = Pattern.compile(SMS_REGEX);
    }
}