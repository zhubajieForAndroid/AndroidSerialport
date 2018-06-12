package com.port.serialport.utils;


import com.port.serialport.base.SerialportApplication;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by dell on 2018/5/24.
 */

public class SerialPortSendUtil {
    public static boolean sendMessage(int[] arr) {
        try {
            OutputStream os = SerialportApplication.getOutputStream();
            if (null == os) return false;
            for (int a : arr) {
                os.write(a);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
