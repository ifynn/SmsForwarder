package cn.fynn.sms_transfer.security;

/**
 * @author Fynn
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class RP {

    static {
        System.loadLibrary("rpb");
    }

    public native static String genRPB();
}