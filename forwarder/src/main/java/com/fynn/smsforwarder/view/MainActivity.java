package com.fynn.smsforwarder.view;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.fynn.smsforwarder.base.ActivityCapacity;
import com.fynn.smsforwarder.base.BaseActivity;
import com.fynn.smsforwarder.business.sms.TransferService;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        if (i != null) {
            i.putExtra(ActivityCapacity.CAPACITY_NAME, MainCapacity.class.getName());
        }

        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_SMS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }

        TransferService.start(this);
    }
}
