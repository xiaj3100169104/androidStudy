package com.style.service.remote;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.style.framework.IRemotePlayService;

import example.music.entity.MediaBean;

/**
 * Created by xiajun on 2018/1/8.
 */

public class RemotePlayService extends Service {
    String TAG = "RemotePlayService";

    public IRemotePlayService.Stub myBinder = new IRemotePlayService.Stub() {
        public int getPid() {
            int pid = android.os.Process.myPid();
            Log.e(TAG, "myPid->" + pid);
            return pid;
        }

        @Override
        public void start(int a) {
            Log.e("MyBinder", "start->" + a);
            //jvm能申请的最大内存
            Log.e("maxMemory", Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M");
            //jvm已经申请到的内存
            Log.e("totalMemory", Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M");
            //jvm剩余空闲内存
            Log.e("freeMemory", Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M");
        }

        @Override
        public void stop(String a) {
            Log.e("MyBinder", "stop->" + a);

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }
}
