package com.fynn.smsforwarder.security;

/**
 * @author Fynn
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class RP {

    public native static String genRPB();

    static {
        System.loadLibrary("rpb");
    }
}