package example.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.style.db.user.UserDBManager;
import com.style.manager.AccountManager;
import com.style.manager.AppManager;
import com.style.net.core.HttpActionManager;

import example.greendao.dao.GreenDaoManager;

public class MyApp extends Application {

    protected static MyApp appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;
        AppManager.getInstance().init(appContext);
        AccountManager.getInstance().init(appContext);
        UserDBManager.getInstance().initialize(appContext);
        GreenDaoManager.getInstance().initialize(appContext);
        HttpActionManager.getInstance().init();

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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    //app停止的时候执行的方法，但并不一定会调用。当虚拟机为别的应用程序腾出更大资源空间而终止当前应用程序的时候，是不会执行该方法的。
    public void onTerminate() {
        super.onTerminate();
        System.exit(0);
    }
}
