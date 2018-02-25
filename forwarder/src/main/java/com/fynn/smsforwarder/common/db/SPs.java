package com.fynn.smsforwarder.common.db;

import com.fynn.smsforwarder.base.dao.Storage;
import com.fynn.smsforwarder.model.consts.Consts;
import com.fynn.smsforwarder.security.RPHelper;

import org.fynn.appu.util.CharsUtils;
import org.fynn.appu.util.RSAHelper;

/**
 * @author Fynn
 * @date 18/2/14
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class SPs {

    public static void saveServerHost(String host) {
        Storage.put(Consts.EmailConst.SERVER_HOST, host);
    }

    public static void saveServerPort(String port) {
        Storage.put(Consts.EmailConst.SERVER_PORT, port);
    }

    public static void saveUsername(String username) {
        Storage.put(Consts.EmailConst.USERNAME, username);
    }

    public static void saveEmail(String email) {
        Storage.put(Consts.EmailConst.EMAIL, email);
    }

    public static void saveSSL(boolean ssl) {
        Storage.put(Consts.EmailConst.SSL, ssl);
    }

    public static void saveBatteryNotify(boolean notify) {
        Storage.put(Consts.EmailConst.BATTERY_NOTIFY, notify);
    }

    public static void savePassword(String password) {
        Storage.put(Consts.EmailConst.PASSWORD,
                RSAHelper.cipher(RPHelper.getPRK(), password));
    }

    public static String getServerHost() {
        return Storage.getString(Consts.EmailConst.SERVER_HOST, "");
    }

    public static String getServerPort() {
        return Storage.getString(Consts.EmailConst.SERVER_PORT, "");
    }

    public static String getUsername() {
        return Storage.getString(Consts.EmailConst.USERNAME, "");
    }

    public static String getEmail() {
        return Storage.getString(Consts.EmailConst.EMAIL, "");
    }

    public static boolean isEnabledSSL() {
        return Storage.getBoolean(Consts.EmailConst.SSL, true);
    }

    public static boolean isBatteryNotify() {
        return Storage.getBoolean(Consts.EmailConst.BATTERY_NOTIFY, false);
    }

    public static String getPassword() {
        String p = Storage.getString(Consts.EmailConst.PASSWORD, "");

        if (!CharsUtils.isEmptyAfterTrimming(p)) {
            p = RSAHelper.decipher(Consts.PUB_KEY, p);
        }

        return p;
    }
}
