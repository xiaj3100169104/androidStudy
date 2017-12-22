package com.style.net.core2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import example.newwork.response.LoginBean;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xiajun on 2017/12/21.
 */

public class RetrofitImpl {
    protected String TAG = "HttpManager";
    private static String URL_BASE_REMOTE = "http://ws.webxml.com.cn/WebServices/";

    private static RetrofitImpl mRetrofitFactory;
    private static APIFunction mAPIFunction;

    private RetrofitImpl() {
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
               /* .connectTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)
                .readTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)
                .writeTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)
                .addInterceptor(InterceptorUtil.HeaderInterceptor())
                .addInterceptor(InterceptorUtil.LogInterceptor())//添加日志拦截器 */
                .build();
        Gson gson = new GsonBuilder()
                //配置你的Gson
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE_REMOTE)
                // 如是有Gson这类的Converter 一定要放在其它前面
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))//添加gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava转换器
                .client(mOkHttpClient)
                .build();
        mAPIFunction = mRetrofit.create(APIFunction.class);
    }

    public static RetrofitImpl getInstance() {
        if (mRetrofitFactory == null) {
            synchronized (RetrofitImpl.class) {
                if (mRetrofitFactory == null)
                    mRetrofitFactory = new RetrofitImpl();
            }

        }
        return mRetrofitFactory;
    }

    public void login(String userName, String password, BaseObserver<LoginBean> consumer) {
        //Observable<BaseResult<LoginBean>> mObservable = mAPIFunction.login(userName, password);
        //consumer.setObservable(mObservable);
    }

    public void getKuaiDi(String userName, String password, BaseObserver consumer) {
        //这里必须链式调用，如果赋值出来会报主线程请求网络错误
        mAPIFunction.getKuaiDi("yuantong", "11111111111")
                .compose(transformer)
                .subscribe(consumer);
    }

    public void getKuaiDi2(String userName, String password, Observer consumer) {
        //这里必须链式调用，如果赋值出来会报主线程请求网络错误
        mAPIFunction.getKuaiDi2("yuantong", "11111111111")
                .compose(transformer)
                .subscribe(consumer);
    }

    private static ObservableTransformer transformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(@NonNull Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())// 取消网络请求放在io线程
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

}
