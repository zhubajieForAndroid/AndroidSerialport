package com.port.androidserialport;


import android.widget.TextView;

import com.port.serialport.base.SerialportApplication;

import java.util.Arrays;

public class MainActivity extends BaseActivity {

    private TextView mDataView;

    @Override
    protected int getResId() {
        return R.layout.activity_main;
    }
    @Override
    protected void init() {
        super.init();
        //串口初始化最好放在程序的入口Activity中,不要放在Application中,因为在程序没有完全退出的时候再启动程序不一定会调用onCreate方法
        SerialportApplication.initSerialPort();
        mDataView = (TextView) findViewById(R.id.result_data);
    }
    @Override
    public void onDataReceived(byte[] buffer, int size) {
        mDataView.setText(Arrays.toString(buffer));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SerialportApplication.closeSerialPort();
    }
}
