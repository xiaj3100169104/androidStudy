package com.style.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log

/**
 * Created by xiajun on 2018/8/7.
 */

class AppManager {

    val TAG = "AppManager"
    private var activityCount: Int = 0
    private var isRunInBackground: Boolean = false
    private var app: Application? = null


    companion object {
        private val mLock = Any()
        private var mInstance: AppManager? = null

        fun getInstance(): AppManager {
            synchronized(mLock) {
                if (mInstance == null) {
                    mInstance = AppManager()
                }
                return mInstance as AppManager
            }
        }

    }

    /* 私有构造方法，防止被JAVA默认的构造函数实例化 */
    private constructor()

    fun init(app: Application) {
        this.app = app
        initBackgroundCallBack(this.app)
    }

    private fun initBackgroundCallBack(app: Application?) {
        app!!.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                logActivityLifecycle(activity, "onActivityCreated")
            }

            override fun onActivityStarted(activity: Activity) {
                logActivityLifecycle(activity, "onActivityStarted")
                activityCount++
                if (isRunInBackground) {
                    //应用从后台回到前台 需要做的操作
                    back2App(activity)
                }
            }

            override fun onActivityResumed(activity: Activity) {
                logActivityLifecycle(activity, "onActivityResumed")
            }

            override fun onActivityPaused(activity: Activity) {
                logActivityLifecycle(activity, "onActivityPaused")
            }

            override fun onActivityStopped(activity: Activity) {
                logActivityLifecycle(activity, "onActivityStopped")
                activityCount--
                if (activityCount == 0) {
                    //应用进入后台 需要做的操作
                    leaveApp(activity)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                logActivityLifecycle(activity, "onActivitySaveInstanceState")
            }

            override fun onActivityDestroyed(activity: Activity) {
                logActivityLifecycle(activity, "onActivityDestroyed")
            }
        })
    }

    /**
     * 从后台回到前台需要执行的逻辑
     *
     * @param activity
     */
    private fun back2App(activity: Activity) {
        isRunInBackground = false
        Log.e(TAG, "app 可见")
    }

    /**
     * 离开应用 压入后台或者退出应用
     *
     * @param activity
     */
    private fun leaveApp(activity: Activity) {
        Log.e(TAG, "app 不可见")
        isRunInBackground = true
    }

    fun logActivityLifecycle(activity: Activity, msg: String) {
        logI(TAG, activity.javaClass.simpleName + "  " + msg)
    }

    fun logI(tag: String, msg: String) {
        LogManager.logI(tag, msg)
    }

}
