package com.port.serialport.factory;

/**
 * 线程池代理工厂,封装线程池代理的创建过程
 * Created by dell on 2018/6/11.
 */

public class ThreadPoolProxyFactory {
    private static ThreadPoolProxy mThreadPoolProxy;
    public static ThreadPoolProxy getmThreadPoolProxy(){
        if (mThreadPoolProxy == null){
            mThreadPoolProxy = new ThreadPoolProxy(1);
        }
        return mThreadPoolProxy;
    }
}
