package com.style.threadPool.callback;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;


/**
 * Created by xiajun on 2017/2/14.
 */

public abstract class CustomFutureTask<T> implements Runnable {
    private static final int TASK_START = 2;
    private static final int TASK_PROGRESS = 3;
    private static final int TASK_COMPLETE = 4;
    private static final int TASK_FAILED = 5;

    private static final String TAG = CustomFutureTask.class.getSimpleName();
    private T data;

    public abstract T doInBackground();
    public abstract void onStart();

    public abstract void onSuccess(T data);

    public abstract void onFailed(String message);

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TASK_START:
                    if (isCallbackEnable()) {
                        /*Bundle bundle = msg.getData();
                        fileDownCallback.start(bundle.getInt("fileSize"));*/
                        onStart();
                    }
                    break;
                case TASK_COMPLETE:
                    if (isCallbackEnable()) {
                        /*Bundle bundle = msg.getData();
                        int currentDownSize = bundle.getInt("currentDownSize");
                        int fileSize = bundle.getInt("fileSize");
                        float progress = bundle.getFloat("progress");
                        fileDownCallback.inProgress(currentDownSize, fileSize, progress);*/
                       onSuccess(data);
                    }
                    break;
                case TASK_FAILED:
                    if (isCallbackEnable()) {
                        /*Bundle bundle = msg.getData();
                        fileDownCallback.complete(bundle.getString("filePath"));*/
                        onFailed("");
                    }
                    break;
            }
        }
    };

    @Override
    public void run() {
        Message msg = mHandler.obtainMessage(TASK_START);
        mHandler.sendMessage(msg);
        //真正的任务在这里执行，这里的返回值类型为String，可以为任意类型
        data = doInBackground();
        // 通知handler去更新视图组件
        Message msg2 = mHandler.obtainMessage(TASK_COMPLETE);
        //msg2.getData().putSerializable("data", (Serializable) data);
        mHandler.sendMessage(msg2);
    }

    public boolean isCallbackEnable() {
        //if (this.canCallback && this.fileDownCallback != null)
        return true;
        // return false;
    }
}
