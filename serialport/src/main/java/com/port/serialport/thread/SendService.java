package com.port.serialport.thread;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.port.serialport.base.SerialportApplication;
import com.port.serialport.utils.SendUtil;

import static android.content.ContentValues.TAG;


/**
 * Created by dell on 2018/3/23.
 * 保持心跳包的服务,程序运行定时1秒发送下面的心跳包
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
            int[] bytes = new int[]{0x2A, 0x06, 0x03, 0x02, 0x2D, 0x23};
            SendUtil.sendMessage(bytes);
        }
    };

}
