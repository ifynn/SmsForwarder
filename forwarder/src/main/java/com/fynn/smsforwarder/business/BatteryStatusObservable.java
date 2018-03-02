package com.fynn.smsforwarder.business;

import java.util.Observable;

/**
 * Created by fynn on 2018/3/2.
 */

public class BatteryStatusObservable extends Observable {

    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }
}
