package com.style.threadPool;

import android.util.Log;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 当提交一个新任务到线程池时首先线程池判断基本线程池(corePoolSize)是否已满？没满，创建一个工作线程来执行任务。
 * 满了判断工作队列(workQueue)是否已满？没满，则将新提交的任务存储在工作队列里。
 * 满了判断整个线程池(maximumPoolSize)是否已满？没满，则创建一个新的工作线程来执行任务。
 * 满了，则交给饱和策略来处理这个任务；
 * 如果线程池中的线程数量大于 corePoolSize 时，如果某线程空闲时间超过keepAliveTime，线程将被终止，
 * 直至线程池中的线程数目不大于corePoolSize；如果允许为核心池中的线程设置存活时间，那么核心池中的线程空闲时间超过 keepAliveTime，线程也会被终止。
 * <p>
 * RejectedExecutionHandler：饱和策略
 * 当队列和线程池都满了，说明线程池处于饱和状态，那么必须对新提交的任务采用一种特殊的策略来进行处理。
 * 这个策略默认配置是AbortPolicy，表示无法处理新的任务而抛出异常。
 * JAVA提供了4中策略：
 * 1、AbortPolicy：直接抛出异常
 * 2、CallerRunsPolicy：只用调用所在的线程运行任务
 * 3、DiscardOldestPolicy：丢弃队列里最近的一个任务，并执行当前任务。
 * 4、DiscardPolicy：不处理，丢弃掉。
 */

public class CustomFileDownloadThreadPoolExecutor extends ThreadPoolExecutor {
    private static final String TAG = CustomFileDownloadThreadPoolExecutor.class.getSimpleName();

    // 线程池核心线程数
    private static final int CORE_POOL_SIZE = 5;
    // 线程池最大线程数
    private static final int MAX_POOL_SIZE = 10;
    // 额外线程空状态生存时间
    private static final int KEEP_ALIVE_TIME = 2;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    // 阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(5);
    //线程工厂
    private static ThreadFactory threadFactory = Executors.defaultThreadFactory();
    //饱和策略(可能线程池被关闭了)
    private static RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            Log.e(TAG, "rejectedExecution");
        }
    };

    public CustomFileDownloadThreadPoolExecutor() {
        this(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, workQueue, threadFactory, handler);
    }

    public CustomFileDownloadThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * 当线程池调用该方法时,线程池的状态则立刻变成SHUTDOWN状态。此时，则不能再往线程池中添加任何任务，否则将会抛出RejectedExecutionException异常。
     * 但是，此时线程池不会立刻退出，直到添加到线程池中的任务都已经处理完成，才会退出。
     */
    @Override
    public void shutdown() {
        super.shutdown();
    }

    /**
     * 根据JDK文档描述，大致意思是：执行该方法，线程池的状态立刻变成STOP状态，
     * 并试图停止所有正在执行的线程，不再处理还在池队列中等待的任务，当然，它会返回那些未执行的任务。
     * 它试图终止线程的方法是通过调用Thread.interrupt()方法来实现的，但是大家知道，这种方法的作用有限，
     * 如果线程中没有sleep 、wait、Condition、定时锁等应用, interrupt()方法是无法中断当前的线程的。所以，ShutdownNow()并不代表线程池就一定立即就能退出，
     * 它可能必须要等待所有正在执行的任务都执行完成了才能退出。
     * 返回正在等待执行的任务。
     */
    @Override
    public List<Runnable> shutdownNow() {
        return super.shutdownNow();
    }
}
