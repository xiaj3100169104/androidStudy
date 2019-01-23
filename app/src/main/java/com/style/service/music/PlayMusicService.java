package com.style.service.music;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import example.music.entity.MediaBean;

/**
 * service通过Context.startService()方法开始，通过Context.stopService()方法停止；
 * 也可以通过Service.stopSelf()方法或者Service.stopSelfResult()方法来停止自己。只要调用一次stopService()方法便可以停止服务，
 * 无论之前它被调用了多少次的启动服务方法。
 * 客户端建立一个与Service的连接，并使用此连接与Service进行通话，通过Context.bindService()方法来绑定服务，Context.unbindService()方法来关闭服务。
 * 多个客户端可以绑定同一个服务，如果Service还未被启动，bindService()方法可以启动服务。
 * 上面startService()和bindService()两种模式是完全独立的。你可以绑定一个已经通过startService()方法启动的服务。
 * 例如：一 个后台播放音乐服务可以通过startService(intend)对象来播放音乐。
 * 可能用户在播放过程中要执行一些操作比如获取歌曲的一些信息，
 * 此时 activity可以通过调用bindServices()方法与Service建立连接(不同进程需要ServiceConnection对象)。更加方便通信。
 * 这种情况下，stopServices()方法实际上不会停止 服务，直到最后一次绑定关闭。
 * onBind(一个activity只能一次，不可多次绑定，且销毁时必须手动unBind)。
 */

public class PlayMusicService extends Service {
    String TAG = "PlayMusicService";
    private MyBinder myBinder = new MyBinder();

    public class MyBinder extends Binder {
        public void start(int p) {
            Log.e("MyBinder", "start->" + p);
        }

        public void play(MediaBean m) {
            Log.e("MyBinder", "play->" + m.path);
            Application a = getApplication();
            Context c = a.getApplicationContext();
            Context c1 = getApplicationContext();
        }

        public void stop(String s) {
            Log.e("MyBinder", "stop->" + s);
        }
    }

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
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    //startService方式启动才会执行这个方法
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
