package com.fynn.smsforwarder.base;

import android.app.Application;

import com.fynn.smsforwarder.business.crash.CrashManager;

import org.fynn.appu.AppU;

/**
 * Created by fynn on 2018/2/4.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppU.init(this);
        CrashManager.register(this);
    }
}
