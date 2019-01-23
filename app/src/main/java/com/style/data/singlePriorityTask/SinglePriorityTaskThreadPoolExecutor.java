package com.style.data.singlePriorityTask;

import android.util.Log;

import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 单个任务按优先级顺序执行
 */

public class SinglePriorityTaskThreadPoolExecutor extends ThreadPoolExecutor {
    private static final String TAG = SinglePriorityTaskThreadPoolExecutor.class.getSimpleName();
    // 根据CPU数量来配置（设置过大时cpu在多个线程间来回切换执行并不能真正提高效率，意义不大）：参考AsyncTask配置
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    // 线程池核心线程数
    private static final int CORE_POOL_SIZE = 1;
    // 线程池最大线程数
    private static final int MAX_POOL_SIZE = 1;
    // 线程空闲时间
    private static final int KEEP_ALIVE_TIME = 20;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    /**
     * 阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
     * BlockingQueue又是什么呢？它是一个特殊的队列，当我们从BlockingQueue中取数据时，如果BlockingQueue是空的，则取数据的操作会进入到阻塞状态，
     * 当BlockingQueue中有了新数据时，这个取数据的操作又会被重新唤醒。同理，如果BlockingQueue中的数据已经满了，
     * 往BlockingQueue中存数据的操作又会进入阻塞状态，直到BlockingQueue中又有新的空间，存数据的操作又会被冲洗唤醒。
     */
    //如果想顺序执行：令MAX_POOL_SIZE=CORE_POOL_SIZE=1；队列容量根据具体适用场景确定。
    private static BlockingQueue<Runnable> workQueue = new PriorityBlockingQueue<>(128);
    //线程工厂
    private static ThreadFactory threadFactory = Executors.defaultThreadFactory();
    //饱和策略(可能线程池被关闭了)
    private static RejectedExecutionHandler handler = new DiscardPolicy() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            Log.e(TAG, "rejectedExecution");//Thread.MAX_PRIORITY
        }
    };

    public SinglePriorityTaskThreadPoolExecutor() {
        this(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, workQueue, threadFactory, handler);
    }

    public SinglePriorityTaskThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * 关闭线程池，不影响已经提交的任务。此时线程池不会立刻退出，直到添加到线程池中的任务都已经处理完成，才会退出。
     */
    @Override
    public void shutdown() {
        super.shutdown();
    }

    /**
     * 关闭线程池，并尝试去终止正在执行的线程。
     * 如果线程中没有sleep 、wait、Condition、定时锁等应用, interrupt()方法是无法中断当前的线程的。
     * 所以，ShutdownNow()并不代表线程池就一定立即就能退出，
     * 它可能必须要等待所有正在执行的任务都执行完成了才能退出。
     * 返回正在等待执行的任务。
     */
    @Override
    public List<Runnable> shutdownNow() {
        return super.shutdownNow();
    }
}
