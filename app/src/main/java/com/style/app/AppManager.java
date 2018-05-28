package com.style.app;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.style.broadcast.NetWorkChangeBroadcastReceiver;

import java.util.List;

import example.home.MainActivity;


public class AppManager {
    private final String TAG = getClass().getSimpleName();

    private static final String APP_INFO = "appInfo";
    private static final String IS_FIRST_LOGIN = "isFirstLogin";

    public static Typeface TEXT_TYPE;
    private static final Object mLock = new Object();
    private static AppManager mInstance;
    private Context context;

    public synchronized static AppManager getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new AppManager();
            }
            return mInstance;
        }
    }

    private AppManager() {
    }

    public void init(Context context) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(new AppCrashHandler());
        addComponentCallbacks2();

        initReceiver();

    }

    //      监听广播
    private void initReceiver() {

        IntentFilter filter = new IntentFilter(NetWorkChangeBroadcastReceiver.NET_CHANGE);
        this.context.registerReceiver(new NetWorkChangeBroadcastReceiver(), filter);
    }

    public Context getContext() {
        return this.context;
    }

    public SharedPreferences getAppSharedPreferences() {
        SharedPreferences sp = context.getSharedPreferences(APP_INFO, Context.MODE_PRIVATE);
        return sp;
    }

    public void putFirstOpen(boolean isFirstLogin) {
        SharedPreferences.Editor editor = getAppSharedPreferences().edit();
        editor.putBoolean(IS_FIRST_LOGIN, isFirstLogin).apply();
    }

    public boolean isFirstOpen() {
        boolean value = getAppSharedPreferences().getBoolean(IS_FIRST_LOGIN, true);
        return value;
    }

    /**
     * 退出到登录界面
     */
    public void exit2loginInterface() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.channelsoft.cdesk", "com.channelsoft.cdesk.activity.LoginActivity");
        intent.setComponent(componentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    /**
     * 退出app
     */
    public void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void reStartApp() {
        /*Intent i = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getContext().startActivity(i);*/
        Intent mStartActivity = new Intent(context, MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 50, mPendingIntent);
        //System.exit(0);
        exitApp();
    }

    public Class<?> getTopActivity() {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        String className = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        Class<?> cls = null;
        try {
            cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                } else {
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    //在application中使用//不让其他应用接收到广播
    public void sendLocalBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcastSync(intent);
    }

    public void registerLocalReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
    }

    public void unregisterLocalReceiver(BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    private void loadCustomFront() {
        // 加载自定义字体
        try {
            TEXT_TYPE = Typeface.createFromAsset(getContext().getAssets(), "fronts/black_simplified.TTF");
        } catch (Exception e) {
            Log.i("MyApp", "加载第三方字体失败。");
            TEXT_TYPE = null;
        }
    }

    /**
     * 获取app当前的渠道号或application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public String getAppMetaData() {
        String channelNumber = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelNumber = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelNumber;
    }

    private void addComponentCallbacks2() {
        this.context.registerComponentCallbacks(new ComponentCallbacks2() {
            @Override
            public void onTrimMemory(int level) {
                //Log.e("ComponentCallbacks2", "onTrimMemory-->" + level);

            }

            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                //Log.e("ComponentCallbacks2", "onConfigurationChanged-->" + newConfig.toString());

            }

            @Override
            public void onLowMemory() {
                //Log.e("ComponentCallbacks2", "onLowMemory");
            }
        });
    }


}
