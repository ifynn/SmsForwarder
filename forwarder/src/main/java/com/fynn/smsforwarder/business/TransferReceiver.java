package com.fynn.smsforwarder.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Fynn
 * @date 18/2/13
 */
public class TransferReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        /**
         * 启动后台独立进程，负责短信转发
         */
        TransferService.start(context);
    }
}
