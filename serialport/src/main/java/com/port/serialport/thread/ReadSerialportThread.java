package com.port.serialport.thread;

import android.content.Intent;
import android.util.Log;

import com.port.serialport.base.SerialportApplication;
import com.port.serialport.conf.Constants;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dell on 2018/1/2.
 */

public class ReadSerialportThread extends Thread {
    private int mIndex;
    private byte[] mReadBuffer = new byte[128];

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                byte[] resole = resole(SerialportApplication.getInputStream());
                if (resole.length != 1 && resole.length != 2 && resole.length != 3) {
                    // 一个完整包即产生
                    Intent intent = new Intent();
                    intent.setAction(Constants.DATA);
                    intent.putExtra("response", resole);
                    intent.putExtra("size", resole.length);
                    SerialportApplication.getContext().sendBroadcast(intent);
                    intent = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*try {
                byte[] result = getBytes(mInputStream);
                if (result == null) return;
                System.arraycopy(mReadBuffer, 0, result, 0, mIndex);
                Intent intent = new Intent();
                intent.setAction(Constants.DATA);
                intent.putExtra("response", result);
                intent.putExtra("size", result.length);
                SerialportApplication.getContext().sendBroadcast(intent);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }*/
        }
    }

    /**
     * 入参：输入流
     * 返回：byte数组
     * 只校验了结束符
     */
    private byte[] getBytes(InputStream in) throws IOException {
        mIndex = 0;
        if (in == null) {
            return null;
        }
        int i = -1;
        while ((i = in.read()) != -1) {
            mReadBuffer[mIndex] = (byte) i;
            mIndex++;
            if (i == 35) {           //到了一帧数据的结尾,十进制35就是16进制23,在定义的时候23为结束符
                break;
            }
        }
        byte[] result = new byte[mIndex];
        System.arraycopy(mReadBuffer, 0, result, 0, mIndex);
        return result;
    }

    /**
     * 入参：输入流
     * 返回：byte数组
     * 该方法校验了协议的开始符,协议长度,异或值和结束符格式16进制
     * 2A 06 03 02 2D 23   (2a)开始符(06)是协议长度(2D)是异或值(23)结束符
     */
    public byte[] resole(InputStream in) throws IOException {
        byte[] resultTemp = new byte[256]; // 临时接收数组
        // 记录当前读取到第几个字节
        mIndex = 0;
        int dataLength = -1; // 命令长度
        int xor = 0; // 异或值
        int valid = 0; // 有效标识
        int i = -1;
        while ((i = in.read()) != -1) {
            if (i != 42 && mIndex == 0) continue; // 校验命令的开始，i == 42表示开始， index != 0表示已经开始
            mIndex++;
            if (mIndex == dataLength - 1)  // 校验异或位是否正确
                if (xor != i) valid = 1; // 异或校验错误

            xor ^= i; // 并计算异或值
            //resultCommand.add(i); // 并保存当前字节到集合
            resultTemp[mIndex - 1] = (byte) i;

            if (mIndex == 2) // 第2个字节时，记录当前返回命令的长度
                dataLength = i;

            if (mIndex == dataLength) { // 当前长度等于命令长度时结束。并校验结束位是否为23,不等于23则标记返回命令异常
                if (i != 35) valid = 2; // 不是以23结尾
                break;
            }
        }
        if (mIndex != dataLength) valid = 3; // 接收长度错误

        byte[] result;
        if (valid == 0) { // 成功返回命令
            result = new byte[mIndex];
            System.arraycopy(resultTemp, 0, result, 0, mIndex);
        } else { // 失败返回错误码
            result = new byte[1];
            result[0] = (byte) valid;
        }
        return result;
    }
}
