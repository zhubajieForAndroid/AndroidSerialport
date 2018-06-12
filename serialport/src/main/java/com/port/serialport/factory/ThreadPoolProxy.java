package com.port.serialport.factory;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 线程池的代理
 * Created by dell on 2018/6/11.
 */

public class ThreadPoolProxy {
    private int mCorePoolSize;  //核心线程池的大小
    private int mMaximumPoolSize;  //最大线程数
    private ThreadPoolExecutor mExecutor;


    ThreadPoolProxy(int corePoolSize) {
        mCorePoolSize = corePoolSize;
        mMaximumPoolSize = corePoolSize;
    }

    /**
     * 初始化ThreadPoolExecutor对象
     */
    private void initThreadPoolProxy() {
        if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) { //是否关闭是否终止是否为空
            synchronized (ThreadPoolProxy.class) {
                if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
                    long keepAliveTime = 3000;//线程保持的时间
                    TimeUnit unit = TimeUnit.MILLISECONDS;  //保持时间的单位
                    BlockingDeque<Runnable> blockingDeque = new LinkedBlockingDeque<>();//任务队列
                    ThreadFactory factory = Executors.defaultThreadFactory(); //线程工厂
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();//异常捕获器
                    mExecutor = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize, keepAliveTime, unit, blockingDeque, factory, handler);
                }
            }
        }
    }

    /**
     * 提交任务
     *
     * @param task
     * @return
     */
    public Future submit(Runnable task) {
        initThreadPoolProxy();
        Future<?> submit =  mExecutor.submit(task);
        return submit;
    }

    /**
     * 执行任务
     *
     * @param task
     */
    public void execute(Runnable task) {
        initThreadPoolProxy();
        mExecutor.execute(task);
    }

    /**
     * 移除任务
     *  移除任务无效
     * @param task
     */
    public void removeTask(Runnable task) {
        initThreadPoolProxy();
        mExecutor.remove(task);     //只是从队列中删除
    }
    /*
     提交任务和执行任务的区别?
        submit-->有返回值
        execute-->没有返回值
     submit返回回来的Future对象是干嘛的?
        Future:描述异步执行的结果的
            get方法:任务执行完成会接收结果,阻塞等待任务执行完成,得知任务执行过程中可能抛出的异常
            cancle:取消任务的执行
     */
}
