package com.fynn.smsforwarder.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;

/**
 * Created by fynn on 2018/2/4.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityCapacity mActivityCapacity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();

        if (intent == null) {
            return;
        }

        String capacityClass = intent.getStringExtra(ActivityCapacity.CAPACITY_NAME);

        if (TextUtils.isEmpty(capacityClass)) {
            return;
        }

        try {
            mActivityCapacity = (ActivityCapacity) Class.forName(capacityClass).newInstance();
            mActivityCapacity.onActivityCreated(this, savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mActivityCapacity != null) {
            mActivityCapacity.onActivityResumed(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mActivityCapacity != null) {
            mActivityCapacity.onActivityStarted(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mActivityCapacity != null) {
            mActivityCapacity.onActivityPaused(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mActivityCapacity != null) {
            mActivityCapacity.onActivityStopped(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mActivityCapacity != null) {
            mActivityCapacity.onActivitySaveInstanceState(this, outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mActivityCapacity != null) {
            mActivityCapacity.onActivityDestroyed(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mActivityCapacity != null) {
            mActivityCapacity.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = false;

        if (mActivityCapacity != null) {
            handled = mActivityCapacity.onActivityKeyDowned(keyCode, event);
        }

        if (!handled) {
            handled = super.onKeyDown(keyCode, event);
        }

        return handled;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (mActivityCapacity != null) {
            mActivityCapacity.onActivityConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (mActivityCapacity != null) {
            mActivityCapacity.onWindowFocusChanged(hasFocus);
        }
    }
}
