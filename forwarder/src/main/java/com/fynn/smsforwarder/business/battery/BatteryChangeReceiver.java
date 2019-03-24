package com.fynn.smsforwarder.business.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.fynn.appu.util.LogU;

/**
 * 必须通过动态注册才有效
 *
 * @author fynn
 * @date 2018/2/19
 */

public class BatteryChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogU.e("BatteryChangeReceiver onReceive");

        if (intent == null) {
            return;
        }

        String action = intent.getAction();

        if (!Intent.ACTION_BATTERY_CHANGED.equals(action)) {
            return;
        }

        Intent service = (Intent) intent.clone();
        service.setClass(context, BatteryIntentService.class);
        context.startService(service);
    }
}
