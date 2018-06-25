package com.style.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Iterator;
import java.util.List;

/**
 * Created by xiajun on 2017/1/9.
 * app信息工具类
 */

public class AppInfoUtil {
    private final String TAG = this.getClass().getSimpleName();

    public static boolean IsForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        List<ActivityManager.AppTask> appTasks;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            appTasks = am.getAppTasks();
        }
        if (tasks != null && !tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.equals(context)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param rootActivity 目标任务栈的baseActivity
     */
    public static void launchApp(Activity rootActivity) {
        if (IsForeground(rootActivity) == false) {
            ActivityManager am = (ActivityManager) rootActivity.getSystemService(Context.ACTIVITY_SERVICE);
            am.moveTaskToFront(rootActivity.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
        }

    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int verCode = -1;
        try {
            // 注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分
            int pid = android.os.Process.myPid();
            String processAppName = getAppName(context, pid);
            PackageInfo info = context.getPackageManager().getPackageInfo(processAppName, 0);
            verCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verCode;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verName;
    }

    /**
     * check the application process name if process name is not qualified, then we think it is a service process and we will not init SDK
     *
     * @param pID
     * @return
     */
    private static String getAppName(Context context, int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
