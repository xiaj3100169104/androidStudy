package com.style.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.style.framework.BuildConfig;
import com.style.utils.FileUtil;

import java.io.File;
import java.util.List;

/**
 * * 避免跳转传值的key和请求码重复混乱，最好统一放在这里
 * Created by xiajun on 2016/11/25.
 */
public class Skip {
    /**
     * 界面跳转请求码
     */
    public static final int CODE_TAKE_CAMERA = 0x000001;// 拍照
    public static final int CODE_TAKE_ALBUM = 0x000002;// 从相册中选择
    public static final int CODE_PHOTO_CROP = 0x000003;// 系统裁剪头像

    public static File takePhoto(Activity activity, String dir, String name) {
        File f = FileUtil.create(dir, name);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(activity, ConfigUtil.FILE_PROVIDER_AUTHORITY, f);
        } else {
            uri = Uri.fromFile(f);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, CODE_TAKE_CAMERA);
        return f;
    }

    public static void selectPhoto(Activity activity) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, CODE_TAKE_ALBUM);
    }

    /**
     * 退出到登录界面
     */
    public void exit2loginInterface(Context context) {
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


    public Class<?> getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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

}
