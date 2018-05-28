package com.port.serialport.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import android_serialport_api.SerialPort;

import com.port.serialport.thread.ReadThread;
import com.port.serialport.thread.SendService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

/**
 * Created by dell on 2017/9/29.
 */

public class SerialportApplication extends Application {
    private static SerialPort mSerialPort = null;
    private static Handler mHandler;
    private static Context mContext;
    private static InputStream mInputStream;
    private static OutputStream mOutputStream;
    private static ReadThread mReadThread;
    private static Intent mStartIntent;

    public static InputStream getInputStream() {
        return mInputStream;
    }

    public static OutputStream getOutputStream() {
        return mOutputStream;
    }

    public static SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            //串口文件和波特率
            mSerialPort = new SerialPort(new File("/dev/ttyS2"), 19200, 0);
        }
        return mSerialPort;
    }

    /**
     * 关闭串口和服务
     */
    public static void closeSerialPort() {
        if (mStartIntent != null) {
            mContext.stopService(mStartIntent);
        }
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }

        if (null != mInputStream) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != mInputStream) {
                    try {
                        mInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            mInputStream = null;
        }
        if (null != mOutputStream) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (null != mOutputStream) {
                try {
                    mOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mOutputStream = null;
        }
        if (null != mReadThread) {
            mReadThread.interrupt();
            mReadThread = null;
        }
    }


    public static Handler getmHandler() {
        return mHandler;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        mContext = getApplicationContext();
    }

    public static void initSerialPort() {
        //启动发送数据的服务
        mStartIntent = new Intent(mContext, SendService.class);
        mContext.startService(mStartIntent);
        try {
            SerialPort serialPort = getSerialPort();
            mInputStream = serialPort.getInputStream();
            mOutputStream = serialPort.getOutputStream();
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (SecurityException e) {
            Toast.makeText(mContext, "没有串口权限", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(mContext, "无法打开一个未知的串口", Toast.LENGTH_SHORT).show();
        } catch (InvalidParameterException e) {
            Toast.makeText(mContext, "请先配置串口", Toast.LENGTH_SHORT).show();
        }
    }

}
