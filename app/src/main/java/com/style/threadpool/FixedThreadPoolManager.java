package com.style.threadpool;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
 * Created by xiajun on 2017/2/14.
 */

public class FixedThreadPoolManager {
    private static final int MAX_COUNT = 5;
    private Map<String, List<Runnable>> mTaskMap = new HashMap();

    private ExecutorService cachedThreadPool;
    private static FixedThreadPoolManager mInstance;

    public static FixedThreadPoolManager getInstance() {
        if (mInstance == null) {
            mInstance = new FixedThreadPoolManager();
        }
        return mInstance;
    }
    public ExecutorService getThreadPool() {
        if (cachedThreadPool == null)
            cachedThreadPool = Executors.newFixedThreadPool(MAX_COUNT);
        return cachedThreadPool;
    }
    public void runTask(String tag, Callable callBack) {
        getThreadPool().submit(callBack);
    }

    public void runTask(String tag, Runnable callBack) {
        getThreadPool().submit(callBack);
    }
    protected void addTask(String tag, Runnable subscription) {
        /*List<Subscription> mSubscriptions = mTaskMap.get(tag);
        if (mSubscriptions == null)
            mSubscriptions = new ArrayList<>();
        mSubscriptions.add(subscription);
        mTaskMap.put(tag, mSubscriptions);*/
    }

    public void removeTask(String tag) {
       /* List<Runnable> mSubscriptions = mTaskMap.get(tag);
        if (mSubscriptions != null) {
            for (Runnable s : mSubscriptions) {
                if (s != null && s.isUnsubscribed()) {
                    s.unsubscribe();
                }
            }
        }*/
    }
    public void shutdown() {
        //关闭线程池
        if (cachedThreadPool != null)
            cachedThreadPool.shutdown();
    }
}
