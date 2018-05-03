package com.style.app;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.style.db.user.UserDBManager;
import com.style.framework.R;
import com.style.utils.AppInfoUtil;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import example.greendao.dao.GreenDaoManager;
import example.helper.DynamicTimeFormat;

public class MyApp extends Application {
    protected final String TAG = getClass().getSimpleName();

    protected static Context appContext;

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
        initHotfix();
        AppManager.getInstance().init(appContext);
        AccountManager.getInstance().init(appContext);
        UserDBManager.getInstance().initialize(appContext);
        GreenDaoManager.getInstance().initialize(appContext);
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
        appContext = this;
        MultiDex.install(appContext);
        //HotFixManager.getInstance().init(appContext);

    }

    public static Context getAppContext() {
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

    private void initHotfix() {
        String rsaSecret = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC7d1IQLzGZaRGbPvuUDNfCWj/9OIUc5QuowMU8gq9zsf07rgL30bYJmjc15+Jvvu2sPI9o+WzYXzUj8I/N8kek61N/Lvn505+iDQxd1C/F65g+6xci3/aTXwNiOFuzMu1Sg4sFM5/Caln1UthwDedKue9BSy55zi73IEhWm5Xr4YRYPupCP2bTTOl7tOiOf5Mc+qG22hxbDZMxgcobCYGu341HkGnRP13BzD1CpxNmplJanxqCdkrtSd36+5qWJDfx7QTDfZprcDGlMuBKfyQyjXj47lG3uNFR3MtrNdyxY/4Paza06qyQyRxh4UozvmLc1lU8N82Texx2m3VAiesXAgMBAAECggEBAIIgEgt/5srZHsqHTnP28jFLGg24FNX4uz4ZCQ+2mvkQ/HlXkWHZ5KUHsfkuC4SLoEilBNrV/9K1S5q12ewXl9mHK78lqYjbd3wx2Hqs1bhpj5Vk8/tWQL8M/blI64YY/JkBDBfBPMzjq6vZJLmrPPgm1keZWxpbn0gU1YGgn9FJ1pIC7BoUekwGZszmtLH7E00p0hG9budVuas5+UXJHSiCbphJrNOMYzvLLxrYJCTdYdVZCr0o/sWvB/+2I9rsa1y9DOLelrrogv/noD+sP+qrEmNsF8S33V0iRZbpQ6918k1Y3mV13QIUyhnapZuVy23K5rBuOVHMKrlsH/8K+9ECgYEA/UBYR0TiFfbGloQ7kwrhcSy+05bYMUXfjAht+c8WhVqPMqhtrnMIjY7wxAexwMgaFRnUS4777kBACcnED8pcRMJi+llrAlDsKFbJ+4kloypyU+9JW8SCc+tu5I9+TJhtnHZps3MHiCOf7f8SKP8D/cKyVfADJnef8k2BYm6h9B8CgYEAvYAxPsliwtrGuGxGdUzI5l7ASrKSkykB7OLQijUqu1tEIWiWQJeF8r6qDEPe78ijcW7DjGeosBgOsszZlo0t6asx/sEwZ7uHLsE/EEg+SUOD9kDrpBI4zRPdaAfTe61JxUrmq8pRj+xIA18YrDhwoqhrIyGzplCyCq1U7dXM6gkCgYBayWRcOD9sbEkI0GKi9fWXotjN9XePQmM/Sg5SzYBfUfWfzW10alyYkrORMsjwOUvQYLAQXjPGV1YdrC0TFBI7vAvLf56y99uRInvKJoJNmfveRxvfP/UcJTxdx2sZflNdEb4WzJfYoBzHLveNps4BfHh+Akq+0YePT2WgqdR5NQKBgHxtZ+hqI0b+j+0Ya0sqF9/r5yclCclG69S3OWcGcgCDdMDbA4118KXrO4zCI+gePfOFXzEZ/1Fg+oW8rEKGgNclabZ082upXqH8kYQHa55/jm5rQk6BCHrfyEk+mkZjCkVZYQz704JFCyPrlS+//3VOUMrInkVeRwlrtpBUCjDBAoGBAK+HMwPXd+tAqGlomZ/7sSX3KjNzo4OCNsQ4Uj1m+XqotCxkb8vvQZ5OIwfUXjv4QB4EXh0qoQsW6/xU1KPQ91iDezPNyNR+A70C6RxBPRVK6fdDdHAxs8smN7uFobOnTuPtl70nwllZmxC5UxnuEm9G/Ot2io7qBYbIK0t1LX+M";
        SophixManager.getInstance().setContext(this)
                .setAppVersion(AppInfoUtil.getVersionName(this))
                .setAesKey(null)
                .setEnableDebug(true)
                .setSecretMetaData(null, null, rsaSecret)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        String msg = new StringBuilder("").append("Mode:").append(mode)
                                .append(" Code:").append(code)
                                .append(" Info:").append(info)
                                .append(" HandlePatchVersion:").append(handlePatchVersion).toString();
                        Log.e(TAG, "onLoad->" + msg);
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                            SophixManager.getInstance().killProcessSafely();
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();
    }
}
