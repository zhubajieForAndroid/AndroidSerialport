package com.port.serialport.utils;


import com.port.serialport.base.SerialportApplication;

import java.io.IOException;

/**
 * Created by dell on 2018/5/24.
 */

public class SendUtil {
    public static boolean sendMessage(int[] arr) {
        try {
            for (int a : arr) {
                SerialportApplication.getOutputStream().write(a);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
