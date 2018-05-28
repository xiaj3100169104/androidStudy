package com.style.data.net.core2;

import com.style.data.net.bean.KuaiDi;
import com.style.data.net.core2.request.LoginRequest;
import com.style.data.net.core2.response.BaseResult;
import com.style.data.net.core2.response.TokenResponse;
import com.style.data.net.bean.UserInfo;
import com.style.data.net.core2.converter.FastJsonConverterFactory;
import com.style.data.net.core2.converter.HttpConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import example.newwork.response.LoginBean;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by xiajun on 2017/12/21.
 */

public class RetrofitImpl {
    protected String TAG = this.getClass().getSimpleName();
    private static String URL_BASE = HttpConfig.URL_BASE;

    private static APIFunction mAPIFunction;
    private static final Object mLock = new Object();
    private static RetrofitImpl mInstance;

    public static RetrofitImpl getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new RetrofitImpl();
            }
            return mInstance;
        }
    }

    private RetrofitImpl() {
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)
                .readTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)
                .writeTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)
                .addInterceptor(InterceptorUtil.LogInterceptor())
                .addInterceptor(InterceptorUtil.HeaderInterceptor())
                .build();
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava转换器
                .client(mOkHttpClient)
                .build();
        mAPIFunction = mRetrofit.create(APIFunction.class);
    }

    private static ObservableTransformer transformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(@NonNull Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())// 取消网络请求放在io线程
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public Observable<String> getPhoneInfo(String phone) {
        return mAPIFunction.getMolileLocation(phone, "").compose(transformer);
    }

    public Observable<String> getWeather(String cityCode) {
        return mAPIFunction.getWeatherInfo(cityCode, "").compose(transformer);
    }

    public Observable<BaseResult<LoginBean>> login(String userName, String password) {
        Observable<BaseResult<LoginBean>> mObservable = mAPIFunction.login(userName, password);
        return mObservable.compose(transformer);
    }

    public Observable<String> getKuaiDi(String userName, String password) {
        return mAPIFunction.getKuaiDi("yuantong", "11111111111").compose(transformer);
    }

    public Observable<List<KuaiDi>> getPupil(String guardianId) {
        JSONObject o = new JSONObject();
        try {
            o.put("GuardianId", guardianId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), o.toString());
        return mAPIFunction.getPupil(requestBody);
    }

    public Observable<TokenResponse> getToken() {
        return mAPIFunction.getToken("client_credentials").compose(transformer);
    }

    public Observable<UserInfo> login2(String userName, String passWord) {
        return mAPIFunction.login2(new LoginRequest(userName, passWord)).compose(transformer);
    }

}
