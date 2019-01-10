package com.style.threadPool;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 从结果可以观察出：
 * 1、创建的线程池具体配置为：核心线程数量为5个；全部线程数量为10个；工作队列的长度为5。
 * 2、我们通过queue.size()的方法来获取工作队列中的任务数。
 * 3、运行原理：
 * 刚开始都是在创建新的线程，达到核心线程数量5个后，新的任务进来后不再创建新的线程，而是将任务加入工作队列，
 * 任务队列到达上线5个后，新的任务又会创建新的普通线程，直到达到线程池最大的线程数量10个，后面的任务则根据配置的饱和策略来处理。
 * 我们这里没有具体配置，使用的是默认的配置AbortPolicy:直接抛出异常。
 * 当然，为了达到我需要的效果，上述线程处理的任务都是利用休眠导致线程没有释放！！！
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
    //饱和策略
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
}
