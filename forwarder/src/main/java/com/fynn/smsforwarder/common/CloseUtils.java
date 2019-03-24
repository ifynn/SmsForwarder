package com.fynn.smsforwarder.common;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author lifs
 * @date 2018/6/1
 */
public class CloseUtils {

    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
