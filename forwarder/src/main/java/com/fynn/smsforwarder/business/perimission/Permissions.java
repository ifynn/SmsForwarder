package com.fynn.smsforwarder.business.perimission;

import android.content.pm.PackageManager;

import org.fynn.appu.AppU;

/**
 * @author lifs
 * @date 2018/6/1
 */
public class Permissions {

    public static boolean hasPermission(String pms) {
        int p = AppU.app().checkCallingOrSelfPermission(pms);
        return p == PackageManager.PERMISSION_GRANTED;
    }
}
