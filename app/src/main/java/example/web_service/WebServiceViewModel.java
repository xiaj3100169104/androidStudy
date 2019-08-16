package example.web_service;


import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.style.base.BaseViewModel;
import com.style.data.http.core.HttpExceptionConsumer;
import com.style.data.http.exception.ResultErrorException;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;


public class WebServiceViewModel extends BaseViewModel {

    MutableLiveData<String> content = new MutableLiveData<>();

    public WebServiceViewModel(@NonNull Application application) {
        super(application);
    }

    public void searchWeather(String code) {
        Disposable d = getHttpApi().getWeather(code)
                .subscribe(s -> {
                    showToast("查询天气成功");
                    content.postValue(s);
                }, new HttpExceptionConsumer()
        );
        addTask(d);
    }

    @SuppressLint("CheckResult")
    public void getPhoneInfo(String phone) {
        getHttpApi().test().subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                long code = responseBody.contentLength();
            }
        });
        /*Disposable d = getHttpApi().getPhoneInfo(phone).subscribe(s -> content.postValue(s));
        addTask(d);*/
    }

    public void getWeather(String code) {
        Disposable d = getHttpApi().getWeather(code).subscribe(s -> content.postValue(s));
        addTask(d);
    }

    public void getKuaiDi(String s, String s1) {
        Disposable d = getHttpApi().getKuaiDi("", "")
                .flatMap(kuaiDis -> getHttpApi().getKuaiDi("", ""))
                .subscribe(s2 -> content.postValue(s2)
                        , throwable -> handleHttpError(throwable));
        addTask(d);
    }

    public void login(final String userName, final String pass) {
        Disposable d = getHttpApi().getToken().flatMap(tokenResponse -> {
            if (TextUtils.isEmpty(tokenResponse.access_token))
                throw new ResultErrorException(ResultErrorException.REQUEST_FAILED);
            Log.e(getTAG(), tokenResponse.access_token);
            getPreferences().setSignKey(tokenResponse.access_token);
            return getHttpApi().login2(userName, pass);
        }).subscribe(userInfo -> Log.e(getTAG(), userInfo.toString())
                , throwable -> handleHttpError(throwable)
                , () -> Log.e(getTAG(), "sfdfsd"));
        addTask(d);
    }
}
