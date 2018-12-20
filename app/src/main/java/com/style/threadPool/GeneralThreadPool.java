package com.style.threadPool;

import android.util.Log;

import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.internal.util.LinkedArrayList;

/**
 * Created by xiajun on 2018/3/24.
 */

public class GeneralThreadPool extends ThreadPoolExecutor {
    private static final String TAG = "GeneralThreadPool";

    // 线程池核心线程数
    private static final int CORE_POOL_SIZE = 5;
    // 线程池最大线程数
    private static final int MAX_POOL_SIZE = 10;
    // 额外线程空状态生存时间
    private static final int KEEP_ALIVE_TIME = 2;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    // 阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(7);
    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger atomicInteger = new AtomicInteger();
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "currentThread : " + atomicInteger.getAndIncrement());
        }
    };

    private static RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            Log.e(TAG, "rejectedExecution");
        }
    };

    public GeneralThreadPool(){
        this(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, workQueue, threadFactory, rejectedExecutionHandler);
    }

    public GeneralThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }
}
