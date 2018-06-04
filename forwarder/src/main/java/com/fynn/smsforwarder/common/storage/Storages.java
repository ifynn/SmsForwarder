package com.fynn.smsforwarder.common.storage;

import android.Manifest;
import android.content.Context;
import android.os.Environment;

import com.fynn.smsforwarder.business.perimission.Permissions;

import java.io.File;

/**
 * @author Fynn
 * @date 2018/6/1
 */
public class Storages {

    public static File getCacheDirectory(Context context, String custom) {
        File cacheDir = null;

        boolean mounted = "mounted".equals(Environment.getExternalStorageState());
        boolean hasPms = Permissions.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (mounted && hasPms) {
            File parent = Environment.getExternalStorageDirectory();
            cacheDir = new File(parent, custom);
        }

        if (cacheDir == null || !cacheDir.exists() && !cacheDir.mkdirs()) {
            cacheDir = context.getCacheDir();
        }

        return cacheDir;
    }
}
