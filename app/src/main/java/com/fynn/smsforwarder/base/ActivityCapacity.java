package com.fynn.smsforwarder.base;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by fynn on 2018/2/4.
 */

public interface ActivityCapacity extends Application.ActivityLifecycleCallbacks {

    String CAPACITY_NAME = "capacityName";

    @Override
    void onActivityCreated(Activity activity, Bundle savedInstanceState);

    @Override
    void onActivityStarted(Activity activity);

    @Override
    void onActivityResumed(Activity activity);

    @Override
    void onActivityPaused(Activity activity);

    @Override
    void onActivityStopped(Activity activity);

    @Override
    void onActivitySaveInstanceState(Activity activity, Bundle outState);

    @Override
    void onActivityDestroyed(Activity activity);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onClick(View v);

    boolean onActivityKeyDowned(int keyCode, KeyEvent event);

    void onActivityConfigurationChanged(Configuration newConfig);

    void onWindowFocusChanged(boolean hasFocus);
}
