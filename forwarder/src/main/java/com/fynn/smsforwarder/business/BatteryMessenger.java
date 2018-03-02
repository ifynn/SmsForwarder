package com.fynn.smsforwarder.business;

import android.os.BatteryManager;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author fynn
 * @date 18/3/2
 */
public interface BatteryMessenger {

    /**
     * 处理不同的电量提醒
     *
     * @param status
     * @param percent
     */
    void handle(@Status int status, int percent);

    @IntDef({
            BatteryManager.BATTERY_STATUS_UNKNOWN,
            BatteryManager.BATTERY_STATUS_CHARGING,
            BatteryManager.BATTERY_STATUS_DISCHARGING,
            BatteryManager.BATTERY_STATUS_NOT_CHARGING,
            BatteryManager.BATTERY_STATUS_FULL})
    @Retention(RetentionPolicy.SOURCE)
    @interface Status {
    }
}
