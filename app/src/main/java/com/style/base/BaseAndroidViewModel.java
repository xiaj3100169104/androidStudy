package com.style.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;

import com.style.app.LogManager;
import com.style.app.MyApp;
import com.style.app.ToastManager;
import com.style.bean.User;
import com.style.data.net.core.RetrofitImpl;
import com.style.data.net.exception.HttpThrowableUtil;
import com.style.data.prefs.AccountManager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by xiajun on 2018/7/13.
 */

public class BaseAndroidViewModel extends AndroidViewModel {
    protected final String TAG = this.getClass().getSimpleName();

    private MutableLiveData<Boolean> generalFinish = new MutableLiveData<>();
    private CompositeDisposable tasks = new CompositeDisposable();

    public BaseAndroidViewModel(@NonNull Application application) {
        super(application);
    }

    @NonNull
    @Override
    public MyApp getApplication() {
        return super.getApplication();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.e(TAG, "onCleared");
        tasks.dispose();
    }

    protected AccountManager getPreferences() {
        return AccountManager.getInstance();
    }

    protected RetrofitImpl getHttpApi() {
        return RetrofitImpl.getInstance();
    }

    public MutableLiveData<Boolean> getGeneralFinish() {
        return generalFinish;
    }

    protected void addTask(Disposable d) {
        tasks.add(d);
    }

    protected void removeAllTask() {
        if (tasks != null)
            tasks.dispose();
    }

    protected void showToast(CharSequence str) {
        ToastManager.showToast(getApplication(), str);
    }

    protected void showToast(@StringRes int resId) {
        ToastManager.showToast(getApplication(), resId);
    }

    protected void logI(String tag, String msg) {
        LogManager.logI(tag, msg);
    }

    protected void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }

    public void handleHttpError(Throwable e) {
        HttpThrowableUtil.handleHttpError(getApplication(), e);
    }
}
