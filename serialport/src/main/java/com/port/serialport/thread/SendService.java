package com.port.serialport.thread;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import com.port.serialport.base.SerialportApplication;
import com.port.serialport.utils.SerialPortSendUtil;


/**
 * Created by dell on 2018/3/23.
 */

public class SendService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        SerialportApplication.getmHandler().postDelayed(mRunnable, 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SerialportApplication.getmHandler().removeCallbacks(mRunnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private Runnable mRunnable = new Thread() {
        @Override
        public void run() {
            SerialportApplication.getmHandler().postDelayed(this, 1000);
            int[] bytes = new int[]{0x2a, 0x06, 0x03, 0x01, 0x2E, 0x23};
            SerialPortSendUtil.sendMessage(bytes);
        }
    };

}
