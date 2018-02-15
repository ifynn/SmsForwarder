package com.fynn.smsforwarder.view;

import android.content.Intent;
import android.os.Bundle;

import com.fynn.smsforwarder.base.ActivityCapacity;
import com.fynn.smsforwarder.base.BaseActivity;
import com.fynn.smsforwarder.business.TransferService;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        if (i != null) {
            i.putExtra(ActivityCapacity.CAPACITY_NAME, MainCapacity.class.getName());
        }

        super.onCreate(savedInstanceState);
        TransferService.start(this);
    }
}
