package com.fynn.smsforwarder.business;

import android.app.IntentService;
import android.content.Intent;
import android.os.BatteryManager;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.fynn.smsforwarder.common.db.SPs;

import java.util.Observable;

/**
 *
 * @author fynn
 * @date 2018/2/19
 */

public class BatteryIntentService extends IntentService {

    private Observable observable;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public BatteryIntentService() {
        super("BatteryIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_UNKNOWN);

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if (level < 0 || scale < 0 || scale < level) {
            return;
        }

        int percent = (level * 100) / scale;

        Pair p = Pair.create(status, percent);
        observable.notifyObservers(p);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        boolean notification = SPs.isBatteryNotify();

        if (!notification) {
            return;
        }

        observable = new BatteryStatusObservable();
        observable.addObserver(new LowBatteryMessenger());
        observable.addObserver(new FullBatteryMessenger());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (observable != null) {
            observable.deleteObservers();
            observable = null;
        }
    }
}
