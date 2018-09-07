package com.style.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.support.annotation.StringRes
import android.util.Log

import com.style.app.LogManager
import com.style.app.MyApp
import com.style.app.ToastManager
import com.style.bean.User
import com.style.data.net.core.RetrofitImpl
import com.style.data.net.exception.HttpThrowableUtil
import com.style.data.prefs.AccountManager

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by xiajun on 2018/7/13.
 */

open class BaseAndroidViewModel(application: Application) : AndroidViewModel(application) {
    protected val TAG = this.javaClass.simpleName

    val generalFinish = MutableLiveData<Boolean>()
    private val tasks = CompositeDisposable()

    protected val preferences: AccountManager
        get() = AccountManager.instance

    protected val httpApi: RetrofitImpl
        get() = RetrofitImpl.getInstance()

    override fun onCleared() {
        super.onCleared()
        Log.e(TAG, "onCleared")
        tasks.dispose()
    }

    protected fun addTask(d: Disposable) {
        tasks.add(d)
    }

    protected fun removeAllTask() {
        tasks?.dispose()
    }

    protected fun showToast(str: CharSequence) {
        ToastManager.showToast(getApplication(), str)
    }

    protected fun showToast(@StringRes resId: Int) {
        ToastManager.showToast(getApplication(), resId)
    }

    protected fun logI(tag: String, msg: String) {
        LogManager.logI(tag, msg)
    }

    protected fun logE(tag: String, msg: String) {
        LogManager.logE(tag, msg)
    }

    fun handleHttpError(e: Throwable) {
        HttpThrowableUtil.handleHttpError(getApplication(), e)
    }
}
