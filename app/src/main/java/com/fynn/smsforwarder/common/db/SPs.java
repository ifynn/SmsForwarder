package com.fynn.smsforwarder.common.db;

import android.content.Context;
import android.content.SharedPreferences;

import org.fynn.appu.AppU;

/**
 * @author Fynn
 * @date 18/2/14
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class SPs {

    private static final String HEAD = "spdata";

    public static SharedPreferences.Editor getEditor() {
        SharedPreferences sp = AppU.app().getSharedPreferences(HEAD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        return editor;
    }

    public static SharedPreferences getSharedPreferences() {
        SharedPreferences sp = AppU.app().getSharedPreferences(HEAD, Context.MODE_PRIVATE);
        return sp;
    }
}
