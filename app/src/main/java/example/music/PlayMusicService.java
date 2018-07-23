package example.music;

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
 * Created by xiajun on 2018/1/8.
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

    //startService方式启动才会执行这个方法
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }
}
