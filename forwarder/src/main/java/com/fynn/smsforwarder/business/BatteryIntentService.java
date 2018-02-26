package com.fynn.smsforwarder.business;

import android.app.IntentService;
import android.content.Intent;
import android.os.BatteryManager;
import android.support.annotation.Nullable;

import com.fynn.smsforwarder.common.db.SPs;
import com.fynn.smsforwarder.model.bean.Email;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by fynn on 2018/2/19.
 */

public class BatteryIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public BatteryIntentService() {
        super("BatteryIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        long now = new Date().getTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(
                now - TransferService.mLastBatteryNotify);

        if (minutes <= 15) {
            return;
        }

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_UNKNOWN);

        if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
            return;
        }

        boolean notification = SPs.isBatteryNotify();

        if (!notification) {
            return;
        }

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if (level < 0 || scale < 0 || scale < level) {
            return;
        }

        int percent = (level * 100) / scale;
        String msg;

        if (status == BatteryManager.BATTERY_STATUS_FULL) {
            msg = "已充满电！";
        } else if (percent >= 0 && percent <= 15) {
            msg = "电量过低 (" + percent + "%)，请充电!";
        } else {
            return;
        }

        Email email = EmailTransfer.genEmailData(msg, msg, "短信转移");

        if (email == null) {
            return;
        }

        try {
            EmailTransfer.send(email);
            TransferService.mLastBatteryNotify = now;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
