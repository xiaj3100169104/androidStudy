package com.style.base;

import android.support.annotation.StringRes;

import com.style.app.AppManager;
import com.style.app.ToastManager;
import com.style.data.net.exception.ResultErrorException;
import com.style.data.prefs.AccountManager;
import com.style.data.net.core.RetrofitImpl;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Created by xiajun on 2018/4/29.
 */

public abstract class BaseActivityPresenter<V> {
    protected final String TAG = getClass().getSimpleName();

    private final AccountManager accountManager;
    private final RetrofitImpl httpApi;
    private final CompositeDisposable tasks;
    V mActivity;

    public BaseActivityPresenter(V mActivity) {
        this.mActivity = mActivity;
        accountManager = AccountManager.getInstance();
        httpApi = RetrofitImpl.getInstance();
        tasks = new CompositeDisposable();
    }

    public V getActivity() {
        return mActivity;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public RetrofitImpl getHttpApi() {
        return httpApi;
    }

    public CompositeDisposable getTasks() {
        return tasks;
    }

    protected void addTask(Disposable d) {
        getTasks().add(d);
    }

    public void onDestroy() {
        if (tasks != null)
            tasks.dispose();
        mActivity = null;
    }

    public void handleHttpError(Throwable e) {
        e.printStackTrace();
        //网络断开
        if (e instanceof UnknownHostException) {
            onErrorMsg("网络连接断开");
        } else if (e instanceof SocketTimeoutException) {
            onErrorMsg("网络连接超时");
        } else if (e instanceof HttpException) {
            HttpException he = (HttpException) e;
            switch (he.code()) {
                case HttpsURLConnection.HTTP_UNAUTHORIZED:
                    onTokenError();
                    break;
                case HttpsURLConnection.HTTP_INTERNAL_ERROR:
                    onErrorMsg("服务器繁忙");
                    break;
                case HttpsURLConnection.HTTP_NOT_FOUND:
                    onErrorMsg("跑去火星啦");
                    break;
            }
        } else if (e instanceof ResultErrorException) {
            //请求失败
            onErrorMsg(((ResultErrorException) e).msg);
        }
    }

    protected void onTokenError() {
        onErrorMsg("授权过期，请稍候重试");
        //getMvpView().onError(R.string.str_unauthorized);
        //updateAccessToken();
    }

    private void onErrorMsg(@StringRes int resId) {
        onErrorMsg(AppManager.getInstance().getContext().getString(resId));
    }

    private void onErrorMsg(String msg) {
        ToastManager.showToast(AppManager.getInstance().getContext(), msg);
    }
}
