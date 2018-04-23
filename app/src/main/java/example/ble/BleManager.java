package example.ble;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.style.manager.AccountManager;
import com.style.manager.LogManager;
import com.style.manager.ToastManager;

import example.home.MainActivity;
import io.reactivex.disposables.Disposable;

/**
 * Created by xiajun on 2018/4/17.
 */

public class BleManager {
    private final String TAG = getClass().getSimpleName();
    private BleService mBleService;

    private static final Object mLock = new Object();
    private static BleManager mInstance;
    private MainActivity mContext;

    //避免同时获取多个实例
    public synchronized static BleManager getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new BleManager();
            }
            return mInstance;
        }
    }

    public BleManager() {

    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBleService = ((BleService.BleServiceBinder) service).getBleService();
            Log.e(TAG, "on LeBleService Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void init(MainActivity context) {
        mContext = context;
        mContext.bindService(new Intent(this.mContext, BleService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void connect() {
        mBleService.connect();
    }
    //手动扫描时，把自动连接设为false
    public void scan() {
        mBleService.setAutoConnect(false);
        mBleService.scanDevice();
    }

    public void disconnect() {
        mBleService.setAutoConnect(false);
        mBleService.disconnect();
    }

    public void close() {
        mBleService.setAutoConnect(false);
        mContext.unbindService(mServiceConnection);
        mBleService.stopSelf();
    }

    public void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }

}
