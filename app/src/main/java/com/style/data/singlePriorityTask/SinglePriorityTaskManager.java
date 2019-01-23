package com.style.data.singlePriorityTask;

import java.util.HashMap;
import java.util.concurrent.Future;

/**
 * PriorityQueue 是基于最小堆原理实现。
 * 最小堆是一种经过排序的完全二叉树，其中任一非终端节点的数据值均不大于其左子节点和右子节的值。
 * 完全最小堆的示意图如下（摘自维基百科）：
 *                                    1
 *                               /        \
 *                             2          6
 *                          /  \        /  \
 *                        5    3      8    12
 */
public final class SinglePriorityTaskManager {

    private HashMap<String, Future> mTaskMap = new HashMap<>();
    private SinglePriorityTaskThreadPoolExecutor mThreadPoolExecutor;
    private static final Object mLock = new Object();
    private static volatile SinglePriorityTaskManager mInstance;

    public static SinglePriorityTaskManager getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new SinglePriorityTaskManager();
            }
            return mInstance;
        }
    }

    private SinglePriorityTaskThreadPoolExecutor getThreadPool() {
        if (mThreadPoolExecutor == null)
            mThreadPoolExecutor = new SinglePriorityTaskThreadPoolExecutor();
        return mThreadPoolExecutor;
    }

    public void addTask(String tag, PrioritizedTask task) {
        getThreadPool().execute(task);
    }
}
