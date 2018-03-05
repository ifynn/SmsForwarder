package com.fynn.smsforwarder.business;

import android.os.BatteryManager;

import com.fynn.smsforwarder.model.bean.Email;

import java.util.concurrent.TimeUnit;

/**
 * @author fynn
 * @date 18/3/2
 */
public class FullBatteryMessenger extends BatteryNotify {

    /**
     * 每个阶段提醒次数
     */
    private static final int TIMES_REMIND = 3;

    /**
     * 提醒间隔时间
     */
    private static final int MINUTES_REMIND_GAP = 30;

    /**
     * 上一次提醒毫秒数
     */
    private static long sLastNotifyMilliseconds = 0;

    /**
     * 断开充电之前的提醒次数
     */
    private static int sNotifyTimes = 0;

    @Override
    public void handle(int status, int percent) {
        // 断开数据线
        if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
            synchronized (this) {
                sNotifyTimes = 0;
            }
        }

        boolean notice = isNotice(status, percent);

        if (!notice) {
            return;
        }

        String msg = "已充满电!";
        Email email = EmailTransfer.genEmailData(msg, msg, "短信转移");

        if (email == null) {
            return;
        }

        try {
            EmailTransfer.send(email);
            sLastNotifyMilliseconds = System.currentTimeMillis();

            synchronized (this) {
                sNotifyTimes++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNotice(int status, int percent) {
        long now = System.currentTimeMillis();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(now - sLastNotifyMilliseconds);

        if (minutes <= MINUTES_REMIND_GAP) {
            return false;
        }

        if (sNotifyTimes >= TIMES_REMIND) {
            return false;
        }

        if (status != BatteryManager.BATTERY_STATUS_FULL) {
            return false;
        }

        return true;
    }
}
