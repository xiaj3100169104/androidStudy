package example.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.multidex.MultiDex;
import com.style.base.BaseApp;
import com.style.broadcast.NetWorkChangeBroadcastReceiver;
import com.style.db.user.UserDBManager;
import com.style.manager.AccountManager;
import com.style.manager.AppManager;

public class MyApp extends BaseApp {

    protected static MyApp appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;
        AppManager.getInstance().init(appContext);
        AccountManager.getInstance().init(appContext);
        UserDBManager.getInstance().initialize(appContext);
        initReceiver();
    }

    //dex文件估计和版本有关，如果是5.1版本以上，不用加这个，如果5.1以下不加，会报类找不到（其实类一直在）
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(appContext);
    }

    public static MyApp getAppContext() {
        return appContext;
    }

    //      监听广播
    private void initReceiver() {

        IntentFilter filter = new IntentFilter(NetWorkChangeBroadcastReceiver.NET_CHANGE);
        registerReceiver(new NetWorkChangeBroadcastReceiver(), filter);
    }
}
