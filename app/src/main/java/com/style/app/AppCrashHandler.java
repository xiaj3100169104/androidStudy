package com.style.app;

import android.os.Process;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by xiajun on 2017/12/22.
 * 注意：自定义处理时就不能再主动退出app时执行
 * Process.killProcess(Process.myPid());
 * System.exit(0);
 * 默认崩溃会执行Process.killProcess(Process.myPid());
 * 向底层发送信号Sending signal. PID: 8561 SIG: 9
 */

public class AppCrashHandler implements Thread.UncaughtExceptionHandler {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG, "uncaughtException");

        //读取stacktrace信息
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);
        String errorReport = result.toString();
        Log.e(TAG, errorReport);
        //重启app
        //AppManager.getInstance().exitApp();
        AppManager.getInstance().reStartApp();

    }
}
