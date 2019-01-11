package com.style.threadPool;

import android.text.TextUtils;

import com.style.data.net.file.SingleFileDownloadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 补充一点,比如,单例,我们见得很多的都是双重同步锁,然而许多人忽略一点就是:
 * 真正的双重同步锁,在类声明变量的时候,会加入volite关键字,其目的为了保证其可见性!因为若是没有保证可见性,
 * 完全有可能,创建了实例之后,没有来得及同步数据,而锁已经释放,这时候就会出现多实例了。
 * 注意我在声明的时候用了valatile关键字,其就会保证可见性。
 * 对于多线程里有两点至关重要，一:原子性,二:可见性。
 * 很多时候,不是加几个读写锁之类的就可以避免并发问题,因为只是解决了原子性,而没有解决可见性,两者缺一不可。
 * synchronized另外一个重要的作用:
 * 可保证一个线程的变化(主要是共享数据的变化)被其他线程所看到（保证可见性，完全可以替代Volatile功能），这点确实也是很重要的。
 * <p>
 * 等待唤醒机制与synchronized:
 * 主要指的是notify/notifyAll和wait方法，在使用这3个方法时，必须处于synchronized代码块或者synchronized方法中，
 * 否则就会抛出IllegalMonitorStateException异常，这是因为调用这几个方法前必须拿到当前对象的监视器monitor对象，
 * 也就是说notify/notifyAll和wait方法依赖于monitor对象.
 * 在前面的分析中，我们知道monitor 存在于对象头的Mark Word 中(存储monitor引用指针)，而synchronized关键字可以获取 monitor ，
 * 这也就是为什么notify/notifyAll和wait方法必须在synchronized代码块或者synchronized方法调用的原因。
 * synchronized (obj) {
 * while( condition checking)
 * {
 * obj.wait();
 * }
 * obj.notify();
 * obj.notifyAll();
 * }
 * 需要特别理解的一点是，与sleep方法不同的是wait方法调用完成后，线程将被暂停，但wait方法将会释放当前持有的监视器锁(monitor)，
 * 直到有线程调用notify/notifyAll方法后方能继续执行，而sleep方法只让线程休眠并不释放锁。
 * 同时notify/notifyAll方法调用后，并不会马上释放监视器锁，而是在相应的synchronized(){}/synchronized方法执行结束后才自动释放锁.
 */

public final class CustomFileDownloadManager {
    public static final String FLAG_NEW_DOWNLOAD = "flag_new_download";
    public static final String FLAG_PAUSE_DOWNLOAD = "flag_pause_download";
    public static final String FLAG_CONTINUE_DOWNLOAD = "flag_continue_download";
    public static final String FLAG_BATCH_DOWNLOAD = "flag_batch_download";

    private Map<String, Future> mTaskMap = new HashMap();
    private CustomFileDownloadThreadPoolExecutor mThreadPool;
    private static final Object mLock = new Object();
    private static volatile CustomFileDownloadManager mInstance;

    public static CustomFileDownloadManager getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new CustomFileDownloadManager();
            }
            return mInstance;
        }
    }

    private CustomFileDownloadThreadPoolExecutor getThreadPool() {
        if (mThreadPool == null)
            mThreadPool = new CustomFileDownloadThreadPoolExecutor();
        return mThreadPool;
    }

    public void addDownloadTask(String tag, SingleFileDownloadTask task) {
        Future oldTask = mTaskMap.get(tag);
        if (oldTask == null) {
            Future ftask = getThreadPool().submit(task);
            mTaskMap.put(tag, ftask);
        } else {
            //已经完成或者取消就移除记录，否则说明该任务正在队列中，无须重复添加
            if (oldTask.isDone() || oldTask.isCancelled()) {
                mTaskMap.remove(tag);
                Future ftask = getThreadPool().submit(task);
                mTaskMap.put(tag, ftask);
            }
        }
    }

    public boolean stopDownloadTask(String tag) {
        boolean isCancelled = false;
        if (!TextUtils.isEmpty(tag) && mTaskMap.containsKey(tag)) {
            Future task = mTaskMap.get(tag);
            if (task != null)
                isCancelled = task.cancel(true);
        }
        removeTask(tag);
        return isCancelled;
    }

    public void removeTask(String tag) {
        mTaskMap.remove(tag);
    }

    public void runTask(String tag, Callable callBack) {
        Future<?> ftask = getThreadPool().submit(callBack);
    }

    public void shutdown() {
        //关闭线程池
        if (mThreadPool != null)
            mThreadPool.shutdown();
    }
}
