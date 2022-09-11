package example.subThreadLooper

import android.os.Handler
import android.os.Looper
import android.os.Process
import androidx.annotation.NonNull
import androidx.annotation.Nullable

class MyHandlerThread : Thread() {
    private val lock = java.lang.Object()

    internal var mPriority: Int = 0
    internal var mTid = -1
    internal var mLooper: Looper? = null
    @Nullable
    private var mHandler: Handler? = null

    fun HandlerThread(name: String){
        mPriority = Process.THREAD_PRIORITY_DEFAULT
    }

    /**
     * Constructs a HandlerThread.
     * @param name
     * @param priority The priority to run the thread at. The value supplied must be from
     * [android.os.Process] and not from java.lang.Thread.
     */
    fun HandlerThread(name: String, priority: Int) {
        mPriority = priority
    }

    /**
     * Call back method that can be explicitly overridden if needed to execute some
     * setup before Looper loops.
     */
    protected fun onLooperPrepared() {}

    override fun run() {
        mTid = Process.myTid()
        Looper.prepare()
        synchronized(lock) {
            mLooper = Looper.myLooper()
            this.lock.notifyAll()
        }
        Process.setThreadPriority(mPriority)
        onLooperPrepared()
        Looper.loop()
        mTid = -1
    }

    /**
     * This method returns the Looper associated with this thread. If this thread not been started
     * or for any reason isAlive() returns false, this method will return null. If this thread
     * has been started, this method will block until the looper has been initialized.
     * @return The looper.
     */
    fun getLooper(): Looper? {
        if (!isAlive) {
            return null
        }

        // If the thread has been started, wait until the looper has been created.
        synchronized(lock) {
            while (isAlive && mLooper == null) {
                try {
                    this.lock.wait()
                } catch (e: InterruptedException) {
                }

            }
        }
        return mLooper
    }

    /**
     * @return a shared [Handler] associated with this thread
     * @hide
     */
    @NonNull
    fun getThreadHandler(): Handler {
        if (mHandler == null) {
            mHandler = Handler(getLooper()!!)
        }
        return mHandler as Handler
    }
}