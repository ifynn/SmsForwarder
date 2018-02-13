package com.fynn.smsforwarder.base;

import android.app.Application;
import android.util.Log;

import com.fynn.smsforwarder.business.TransferService;

import org.fynn.appu.AppU;
import org.fynn.appu.util.LogU;

/**
 * Created by fynn on 2018/2/4.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppU.init(this);
        TransferService.start(this);
    }
}
