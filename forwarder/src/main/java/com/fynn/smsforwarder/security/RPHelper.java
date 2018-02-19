package com.fynn.smsforwarder.security;

import com.fynn.smsforwarder.common.Reflects;

/**
 * @author Fynn
 * @date 18/2/13
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public final class RPHelper {

    public static String getPRK() {
        return Reflects.invokeStaticMethod(
                "cn.fynn.sms_transfer.security.RP", "genRPB", null, null);
    }
}
