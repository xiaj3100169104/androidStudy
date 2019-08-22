package example.web_service;


import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.style.base.BaseViewModel;
import com.style.data.http.exception.HttpExceptionConsumer;
import com.style.data.http.function.impl.UserFunctionImpl;
import com.style.data.http.function.impl.WebFunctionImpl;
import com.style.http.exception.HttpResultException;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;


public class WebServiceViewModel extends BaseViewModel {

    MutableLiveData<String> content = new MutableLiveData<>();

    public WebServiceViewModel(@NonNull Application application) {
        super(application);
    }

    public void searchWeather(String code) {
        Disposable d = WebFunctionImpl.getWeather(code)
                .subscribe(s -> {
                            showToast("查询天气成功");
                            content.postValue(s);
                        }, new HttpExceptionConsumer()
                );
        addTask(d);
    }

    @SuppressLint("CheckResult")
    public void getPhoneInfo(String phone) {
        UserFunctionImpl.test().subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                long code = responseBody.contentLength();
            }
        });
        /*Disposable d = getHttpApi().getPhoneInfo(phone).subscribe(s -> content.postValue(s));
        addTask(d);*/
    }

    public void getWeather(String code) {
        Disposable d = WebFunctionImpl.getWeather(code).subscribe(s -> content.postValue(s));
        addTask(d);
    }

    public void getKuaiDi(String s, String s1) {
        Disposable d = WebFunctionImpl.getKuaiDi("", "")
                .flatMap(kuaiDis -> WebFunctionImpl.getKuaiDi("", ""))
                .subscribe(s2 ->
                                content.postValue(s2)
                        , new HttpExceptionConsumer());
        addTask(d);
    }

    public void login(final String userName, final String pass) {
        Disposable d = UserFunctionImpl.getToken().flatMap(r -> {
            if (TextUtils.isEmpty(r.data.access_token))
                throw HttpResultException.serverError();
            Log.e(getTAG(), r.data.access_token);
            getPreferences().setSignKey(r.data.access_token);
            return UserFunctionImpl.login2(userName, pass);
        }).subscribe(userInfo ->
                        Log.e(getTAG(), userInfo.toString())
                , new HttpExceptionConsumer()
                , () -> Log.e(getTAG(), "sfdfsd"));
        addTask(d);
    }
}
