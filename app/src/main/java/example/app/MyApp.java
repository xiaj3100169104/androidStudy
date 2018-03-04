package example.app;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.style.db.user.UserDBManager;
import com.style.framework.R;
import com.style.manager.AccountManager;
import com.style.manager.AppManager;
import com.style.net.core.HttpActionManager;

import example.greendao.dao.GreenDaoManager;
import example.helper.DynamicTimeFormat;

public class MyApp extends Application {

    protected static MyApp appContext;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//启用矢量图兼容
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;
        AppManager.getInstance().init(appContext);
        AccountManager.getInstance().init(appContext);
        UserDBManager.getInstance().initialize(appContext);
        GreenDaoManager.getInstance().initialize(appContext);
        HttpActionManager.getInstance().init();
        registerComponentCallbacks(new ComponentCallbacks2() {
            @Override
            public void onTrimMemory(int level) {
                Log.e("ComponentCallbacks2", "onTrimMemory-->" + level);

            }

            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                Log.e("ComponentCallbacks2", "onConfigurationChanged-->" + newConfig.toString());

            }

            @Override
            public void onLowMemory() {
                Log.e("ComponentCallbacks2", "onLowMemory");

            }
        });
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
    public void onTrimMemory(int level) {
        Log.e("MyApp", "onTrimMemory-->" + level);
        super.onTrimMemory(level);
        //TRIM_MEMORY_UI_HIDDEN;刚打开11也是这个值，根本就不准，按home键时都调用，且还有很多内存
    }

    @Override
    public void onLowMemory() {
        Log.e("MyApp", "onLowMemory");
        super.onLowMemory();
    }

    //app停止的时候执行的方法，但并不一定会调用。当虚拟机为别的应用程序腾出更大资源空间而终止当前应用程序的时候，是不会执行该方法的。
    public void onTerminate() {
        super.onTerminate();
        System.exit(0);
    }
}
