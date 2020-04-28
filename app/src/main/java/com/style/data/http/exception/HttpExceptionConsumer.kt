package com.style.data.http.exception

import com.style.data.app.AppActivityManager
import com.style.toast.ToastManager
import com.style.http.exception.HttpResultException
import com.style.http.exception.HttpThrowableUtil
import io.reactivex.functions.Consumer

open class HttpExceptionConsumer : Consumer<Throwable> {
    override fun accept(e: Throwable) {
        val t = HttpThrowableUtil.handleHttpError(e)
        when (t.errorCode) {
            HttpResultException.TOKEN_INVALID -> onTokenError(t)
            HttpResultException.NETWORK_ERROR -> onNetworkError(t)
            else -> onOtherError(t)
        }
    }

    open fun onOtherError(t: HttpResultException) {
        ToastManager.showToast(AppActivityManager.getInstance().getApp(), t.msg)
    }

    open fun onNetworkError(t: HttpResultException) {
        ToastManager.showToast(AppActivityManager.getInstance().getApp(), t.msg)
    }

    open fun onTokenError(t: HttpResultException) {
        ToastManager.showToast(AppActivityManager.getInstance().getApp(), t.msg)
    }
}