package com.style.app;

import android.os.Process;
import android.util.Log;

/**
 * Created by xiajun on 2017/12/22.
 * 由于 ActivityManager 时刻监听着进程，一旦发现进程被非正常Kill(不拦截崩溃时)，它将会试图去重启这个进程。
 * 注意：自定义异常崩溃拦截会导致应用卡死，需要手动杀掉进程
 */

public class AppCrashHandler implements Thread.UncaughtExceptionHandler {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG, "uncaughtException");
        ex.printStackTrace();
        //System.exit(0);         //停止当前程序的虚拟机
        Process.sendSignal(Process.myPid(), Process.SIGNAL_KILL);  //使用给定的PID终止进程
    }
}
