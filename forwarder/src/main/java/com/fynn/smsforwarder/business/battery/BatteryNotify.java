package com.fynn.smsforwarder.business.battery;

import android.util.Pair;

import java.util.Observable;
import java.util.Observer;

/**
 * @author fynn
 * @date 18/3/2
 */
public abstract class BatteryNotify implements BatteryMessenger, Observer {

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Pair) {
            Pair<Integer, Integer> p = (Pair<Integer, Integer>) arg;
            handle(p.first, p.second);
        }
    }
}
