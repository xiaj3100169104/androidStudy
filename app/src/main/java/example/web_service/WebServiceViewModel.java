package example.web_service;


import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.style.base.BaseViewModel;
import com.style.data.http.exception.HttpExceptionConsumer;
import com.style.data.http.function.impl.UserNetSourceImpl;
import com.style.data.http.function.impl.WebNetSourceImpl;
import com.style.http.exception.HttpResultException;

import org.jetbrains.annotations.NotNull;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;


public class WebServiceViewModel extends BaseViewModel {

    MutableLiveData<String> content = new MutableLiveData<>();

    public WebServiceViewModel(@NonNull Application application) {
        super(application);
    }

    public void searchWeather(String code) {
        Disposable d = WebNetSourceImpl.getWeather(code)
                .subscribe(s -> {
                            showToast("查询天气成功");
                            content.postValue(s);
                        }, new HttpExceptionConsumer()
                );
        addTask(d);
    }

    @SuppressLint("CheckResult")
    public void getPhoneInfo(String phone) {
        UserNetSourceImpl.test().subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                long code = responseBody.contentLength();
            }
        });
        /*Disposable d = getHttpApi().getPhoneInfo(phone).subscribe(s -> content.postValue(s));
        addTask(d);*/
    }

    public void getWeather(String code) {
        Disposable d = WebNetSourceImpl.getWeather(code).subscribe(s -> content.postValue(s));
        addTask(d);
    }

    public void getKuaiDi(String s, String s1) {
        Disposable d = WebNetSourceImpl.getKuaiDi("", "")
                .flatMap(kuaiDis -> WebNetSourceImpl.getKuaiDi("", ""))
                .subscribe(s2 ->
                                content.postValue(s2)
                        , new HttpExceptionConsumer() {
                            @Override
                            public void accept(@NotNull Throwable e) {
                                super.accept(e);
                            }

                            @Override
                            public void onOtherError(@NotNull HttpResultException t) {
                                super.onOtherError(t);
                            }
                        });
        addTask(d);
    }

    public void login(final String userName, final String pass) {
        Disposable d = UserNetSourceImpl.getToken().flatMap(r -> {
            if (TextUtils.isEmpty(r.data.access_token))
                throw HttpResultException.serverError();
            Log.e(getTAG(), r.data.access_token);
            getPreferences().setSignKey(r.data.access_token);
            return UserNetSourceImpl.login2(userName, pass);
        }).subscribe(userInfo ->
                        Log.e(getTAG(), userInfo.toString())
                , new HttpExceptionConsumer()
                , () -> Log.e(getTAG(), "sfdfsd"));
        addTask(d);
    }
}
