package test.app;

import android.content.Context;
import android.content.IntentFilter;
import android.support.multidex.MultiDex;

import com.style.base.BaseApp;
import com.style.broadcast.NetWorkChangeBroadcastReceiver;
import com.style.db.msg.MsgDBManager;
import com.style.db.user.UserDBManager;
import com.style.manager.AccountManager;
import com.style.manager.AppManager;
import com.style.netrequest.NetRequest;


public class MyApp extends BaseApp {

    protected static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        AppManager.getInstance().init(getInstance());
        AccountManager.getInstance().init(getInstance());
        UserDBManager.getInstance().initialize(getInstance());
        MsgDBManager.getInstance().init(getInstance());
        initReceiver();
        NetRequest.getInstance().init();
        NetRequest.getInstance().test();
    }

    //dex文件估计和版本有关，如果是5.1版本以上，不用加这个，如果5.1以下不加，会报类找不到（其实类一直在）
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(getInstance());
    }

    public static MyApp getInstance() {
        return myApp;
    }

    //      监听广播
    private void initReceiver() {

        IntentFilter filter = new IntentFilter(NetWorkChangeBroadcastReceiver.NET_CHANGE);
        registerReceiver(new NetWorkChangeBroadcastReceiver(), filter);
    }
}
