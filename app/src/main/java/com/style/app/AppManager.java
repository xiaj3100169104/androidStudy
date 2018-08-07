package com.style.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by xiajun on 2018/8/7.
 */

public class AppManager {
    protected final String TAG = getClass().getSimpleName();
    private int activityCount;
    private boolean isRunInBackground;

    private static final Object mLock = new Object();
    private static AppManager mInstance;
    private Application app;

    public static AppManager getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new AppManager();
            }
            return mInstance;
        }
    }

    /* 私有构造方法，防止被JAVA默认的构造函数实例化 */
    private AppManager() {
    }

    public void init(Application app) {
        this.app = app;
        initBackgroundCallBack(this.app);
    }

    private void initBackgroundCallBack(Application app) {
        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                logActivityLifecycle(activity, "onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                logActivityLifecycle(activity, "onActivityStarted");
                activityCount++;
                if (isRunInBackground) {
                    //应用从后台回到前台 需要做的操作
                    back2App(activity);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
                logActivityLifecycle(activity, "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                logActivityLifecycle(activity, "onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                logActivityLifecycle(activity, "onActivityStopped");
                activityCount--;
                if (activityCount == 0) {
                    //应用进入后台 需要做的操作
                    leaveApp(activity);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                logActivityLifecycle(activity, "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                logActivityLifecycle(activity, "onActivityDestroyed");
            }
        });
    }

    /**
     * 从后台回到前台需要执行的逻辑
     *
     * @param activity
     */
    private void back2App(Activity activity) {
        isRunInBackground = false;
        Log.e(TAG, "app 可见");
    }

    /**
     * 离开应用 压入后台或者退出应用
     *
     * @param activity
     */
    private void leaveApp(Activity activity) {
        Log.e(TAG, "app 不可见");
        isRunInBackground = true;
    }

    protected void logActivityLifecycle(Activity activity, String msg) {
        logI(TAG, activity.getClass().getSimpleName() + "  " + msg);
    }

    protected void logI(String tag, String msg) {
        LogManager.logI(tag, msg);
    }
}
