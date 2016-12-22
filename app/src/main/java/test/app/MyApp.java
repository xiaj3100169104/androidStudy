package test.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.style.base.BaseApp;
import com.style.manager.AccountManager;
import com.style.manager.AppManager;


public class MyApp extends BaseApp {

    protected static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        AppManager.getInstance().init(getInstance());
        AccountManager.getInstance().init(getInstance());

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
}
