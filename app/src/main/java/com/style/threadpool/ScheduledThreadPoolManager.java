package com.style.threadpool;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 创建一个定长线程池，支持定时及周期性任务执行。
 * Created by xiajun on 2017/2/14.
 */

public class ScheduledThreadPoolManager {
    private static final int MAX_COUNT = 5;
    private Map<String, List<Runnable>> mTaskMap = new HashMap();

    private ScheduledExecutorService cachedThreadPool;
    private static ScheduledThreadPoolManager mInstance;

    public static ScheduledThreadPoolManager getInstance() {
        if (mInstance == null) {
            mInstance = new ScheduledThreadPoolManager();
        }
        return mInstance;
    }
    public ExecutorService getThreadPool() {
        if (cachedThreadPool == null)
            cachedThreadPool = Executors.newScheduledThreadPool(MAX_COUNT);
        return cachedThreadPool;
    }
    public void runTask(String tag, Callable callBack) {
        getThreadPool().submit(callBack);
        /*ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        scheduledThreadPool.schedule(new Runnable() {

            @Override
            public void run() {
                System.out.println("delay 3 seconds");
            }
        }, 3, TimeUnit.SECONDS);
        表示延迟3秒执行。

        定期执行示例代码如下：
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                System.out.println("delay 1 seconds, and excute every 3 seconds");
            }
        }, 1, 3, TimeUnit.SECONDS);
        表示延迟1秒后每3秒执行一次。
        ScheduledExecutorService比Timer更安全，功能更强大，后面会有一篇单独进行对比。*/
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
