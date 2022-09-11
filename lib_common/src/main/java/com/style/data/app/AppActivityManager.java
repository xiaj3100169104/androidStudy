package com.style.data.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.style.utils.LogManager;

/**
 * Created by xiajun on 2018/8/7.
 */

public class AppActivityManager {

    private static String TAG = "AppActivityManager";
    private int activityCount = 0;
    private boolean isRunInBackground = false;
    private Application app;
    private int taskId = -1;
    private int mainTaskId = -1;

    private static Object mLock = new Object();
    private static AppActivityManager mInstance;

    public static AppActivityManager getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new AppActivityManager();
            }
            return mInstance;
        }
    }

    /* 私有构造方法，防止被JAVA默认的构造函数实例化 */

    public AppActivityManager() {
    }

    public void init(Application app) {
        this.app = app;
        initBackgroundCallBack(this.app);
    }

    public Application getApp() {
        return app;
    }

    private void initBackgroundCallBack(Application app) {
        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
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
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
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
    @SuppressLint("MissingPermission")
    private void back2App(Activity activity) {
        isRunInBackground = false;
        Log.e(TAG, "app 可见");
        if (this.taskId != -1) {
            ActivityManager activityManager = (ActivityManager) this.app.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.moveTaskToFront(taskId, ActivityManager.MOVE_TASK_WITH_HOME);
        }
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

    private void logActivityLifecycle(Activity activity, String msg) {
        logI(TAG, "taskId  " + activity.getTaskId() + "   " + activity.getClass().getSimpleName() + "  " + msg);
    }

    private void logI(String tag, String msg) {
        LogManager.logI(tag, msg);
    }

    @SuppressLint("MissingPermission")
    public void setTestTaskId(int taskId) {
        this.taskId = taskId;
        if (mainTaskId != -1 && this.taskId == -1) {
            ActivityManager activityManager = (ActivityManager) this.app.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.moveTaskToFront(mainTaskId, ActivityManager.MOVE_TASK_NO_USER_ACTION);
        }
    }

    public void setMainTaskId(int taskId) {
        this.mainTaskId = taskId;
    }

}
