package com.style.threadpool;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，
 * 保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
 * 结果依次输出，相当于顺序执行各个任务。
 * Created by xiajun on 2017/2/14.
 */

public class SingleThreadPoolManager {
    private Map<String, List<Runnable>> mTaskMap = new HashMap();

    private ExecutorService cachedThreadPool;
    private static SingleThreadPoolManager mInstance;

    public static SingleThreadPoolManager getInstance() {
        if (mInstance == null) {
            mInstance = new SingleThreadPoolManager();
        }
        return mInstance;
    }
    public ExecutorService getThreadPool() {
        if (cachedThreadPool == null)
            cachedThreadPool = Executors.newSingleThreadScheduledExecutor();
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
