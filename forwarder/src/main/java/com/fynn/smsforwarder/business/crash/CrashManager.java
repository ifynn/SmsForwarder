package com.fynn.smsforwarder.business.crash;

import android.content.Context;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author lifs
 * @date 2018/6/1
 */
public class CrashManager {

    /**
     * 注册 Crash Handler
     *
     * @param context
     */
    public static void register(Context context) {
        Thread.UncaughtExceptionHandler dftHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(context, dftHandler));
    }

    static class CrashHandler implements UncaughtExceptionHandler {

        private Context context;
        private UncaughtExceptionHandler handler;

        public CrashHandler(Context context, UncaughtExceptionHandler handler) {
            this.context = context;
            this.handler = handler;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            CrashCache.writeCrash(context, t, e);

            if (handler != null) {
                handler.uncaughtException(t, e);
            }
        }
    }
}
