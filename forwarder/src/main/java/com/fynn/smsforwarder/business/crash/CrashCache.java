package com.fynn.smsforwarder.business.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.util.Log;

import com.fynn.smsforwarder.common.CloseUtils;
import com.fynn.smsforwarder.common.storage.Storages;

import org.fynn.appu.util.DateHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author lifs
 * @date 2018/6/1
 */
public class CrashCache {

    /**
     * 写入 crash 日志到手机存储中
     *
     * @param context
     * @param thread
     * @param tr
     */
    public static void writeCrash(Context context, Thread thread, Throwable tr) {
        String d = DateHelper.formatDate(new Date(), "yyyy-MM-dd_HH-mm-ss");
        String name = "crash_" + d + ".txt";
        File dir = Storages.getCacheDirectory(context, "/smsforwarder/crash");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File log = new File(dir.getAbsolutePath() + File.separator + name);
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(log, true));
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            String logMessage = String
                    .format("%s\r\n\r\nThread: %d\r\n\r\nMessage: %s\r\n\r\nManufacturer: %s\r\nModel: %s\r\nProduct: %s\r\n\r\nAndroid Version: %s\r\nAPI Level: %d\r\nHeap Size: %sMB\r\n\r\nVersion Code: %s\r\nVersion Name: %s\r\n\r\nStack Trace:\r\n\r\n%s",
                            new Date(), thread.getId(), tr.getMessage(),
                            Build.MANUFACTURER, Build.MODEL, Build.PRODUCT,
                            Build.VERSION.RELEASE, Build.VERSION.SDK_INT,
                            Runtime.getRuntime().maxMemory() / 1024 / 1024,
                            pi.versionCode, pi.versionName,
                            Log.getStackTraceString(tr));

            writer.print(logMessage);
            writer.print("\n\n");

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            CloseUtils.close(writer);
        }
    }
}
