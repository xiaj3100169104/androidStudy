package com.style.data.fileDown;

import android.util.Log;

import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 一般说来，大家认为线程池的大小经验值应该这样设置：（其中N为CPU的核数）
 * 如果是CPU密集型应用，则线程池大小设置为N+1
 * 如果是IO密集型应用，则线程池大小设置为2N+1
 * <p>
 * 那么我们的 Android 应用是属于哪一种应用呢？看下他们的定义。
 * <p>
 * I/O密集型:
 * I/O bound 指的是系统的CPU效能相对硬盘/内存的效能要好很多，此时，系统运作，大部分的状况是 CPU 在等 I/O (硬盘/内存) 的读/写，此时 CPU Loading 不高。
 * CPU-bound:
 * CPU bound 指的是系统的 硬盘/内存 效能 相对 CPU 的效能 要好很多，此时，系统运作，大部分的状况是 CPU Loading 100%，CPU 要读/写 I/O (硬盘/内存)，
 * I/O在很短的时间就可以完成，而 CPU 还有许多运算要处理，CPU Loading 很高。
 * <p>
 * 我们的Android 应用的话应该是属于IO密集型应用，所以数量一般设置为 2N+1。
 * <p>
 * 1.execute一个线程之后，如果线程池中的线程数未达到核心线程数，则会立马启用一个核心线程去执行(即使有线程空闲).
 * 设置allowCoreThreadTimeout=true（默认false）时，核心线程会受线程空闲时间影响超时关闭.
 * <p>
 * 2.execute一个线程之后，如果线程池中的线程数已经达到核心线程数，且workQueue未满，则将新线程放入workQueue中等待执行.
 * <p>
 * 3.execute一个线程之后，如果线程池中的线程数已经达到核心线程数但未超过非核心线程数，且workQueue已满，则开启一个非核心线程来执行任务.
 * <p>
 * 4.execute一个线程之后，如果线程池关闭或者线程池中的线程数已经超过最大线程数，则拒绝执行该任务.
 * <p>
 * RejectedExecutionHandler：饱和策略
 * 当队列和线程池都满了，说明线程池处于饱和状态，那么必须对新提交的任务采用一种特殊的策略来进行处理。
 * 这个策略默认配置是AbortPolicy，表示无法处理新的任务而抛出异常。
 * JAVA提供了4中策略：
 * 1、AbortPolicy：直接抛出异常
 * 2、CallerRunsPolicy：只用调用所在的线程运行任务
 * 3、DiscardOldestPolicy：丢弃队列里最近的一个任务，并执行当前任务。
 * 4、DiscardPolicy：不处理，丢弃。
 */

public class CustomFileDownloadThreadPoolExecutor extends ThreadPoolExecutor {
    private static final String TAG = CustomFileDownloadThreadPoolExecutor.class.getSimpleName();
    // 根据CPU数量来配置（设置过大时cpu在多个线程间来回切换执行并不能真正提高效率，意义不大）：参考AsyncTask配置
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    // 线程池核心线程数
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    // 线程池最大线程数
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
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
    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(128);
    //线程工厂
    private static ThreadFactory threadFactory = Executors.defaultThreadFactory();
    //饱和策略(可能线程池被关闭了)
    private static RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            Log.e(TAG, "rejectedExecution");//Thread.MAX_PRIORITY
        }
    };

    public CustomFileDownloadThreadPoolExecutor() {
        this(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, workQueue, threadFactory, handler);
    }

    public CustomFileDownloadThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(task);
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
    /**
     * 线程池中线程复用就是利用线程sleep来做超时检测。
     * 1.public static boolean interrupted();
     * // 检测当前线程是否已经中断，此方法会清除中断状态，也就是说，假设当前线程中断状态为true，
     * 第一次调此方法，将返回true，表明的确已经中断了，但是第二次调用后，将会返回true，因为第一次调用的操作已将中断状态重新置为false了。
     *
     * 2.public boolean isInterrupted() ;
     * // 检测当前线程是否已经中断，此方法与上一方法的区别在于此方法不会清除中断状态。
     *
     * 3.public void interrupt();
     * //将线程中断状态设置为true，表明此线程目前是中断状态。此时如果调用isInterrupted方法，将会得到true的结果。
     *
     * 通过上述方法的解释，我们可以得出这样的一个结论：interrupt方法本质上不会进行线程的终止操作的，
     * 它不过是改变了线程的中断状态。而改变了此状态带来的影响是，部分可中断的线程方法（比如Object.wait, Thread.sleep）会定期执行isInterrupted方法，
     * 检测到此变化，随后会停止阻塞并抛出InterruptedException异常。但这是否意味着随后线程的退出呢？
     * InterruptedException异常的抛出并不是意味着线程必须得终止，它只是提醒当前线程有中断操作发生了，接下来怎么处理完全取决于线程本身，一般有3种处理方式：
     * 1.“吞并”异常，当做什么事都没发生过。
     * 2.继续往外抛出异常。
     * 3.其它方式处理异常。其它处理异常的方式就有很多种了，停止当前线程或者输出异常信息等等。
     */
}
