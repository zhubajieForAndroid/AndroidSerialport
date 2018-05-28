package com.port.androidserialport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.port.serialport.conf.Constants;

public abstract class BaseActivity extends AppCompatActivity {

    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResId());
        init();
    }

    protected void init() {

    }

    protected abstract int getResId();

    /**
     * 注册串口通讯广播
     */
    public void regBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final byte[] buffer = intent.getByteArrayExtra("response");
                final int size = intent.getIntExtra("size", 0);
                onDataReceived(buffer, size);
            }
        };
        IntentFilter filter = new IntentFilter(Constants.DATA);
        registerReceiver(mBroadcastReceiver, filter);
    }
    public abstract void onDataReceived(byte[] buffer, int size);

    @Override
    protected void onStart() {
        super.onStart();
        regBroadcast();//注册串口通讯广播接受者
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcastReceiver);
    }
}
