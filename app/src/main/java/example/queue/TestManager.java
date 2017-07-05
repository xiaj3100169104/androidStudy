package example.queue;

import android.util.Log;

import java.util.concurrent.BlockingQueue;

/**
 * Created by xiajun on 2017/7/5.
 */

public class TestManager {
    private static final String TAG = "TestManager";
    private static TestManager instance;
    //BlockingQueue<>

    public static synchronized TestManager getInstance() {
        if (instance == null)
            instance = new TestManager();
        return instance;
    }

    public synchronized void test(int x) {
        Log.e(TAG, x + "");
    }
}
