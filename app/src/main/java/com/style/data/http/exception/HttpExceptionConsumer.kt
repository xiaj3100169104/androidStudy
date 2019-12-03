package com.style.data.http.exception

import com.style.app.MyApp
import com.style.data.app.AppManager
import com.style.toast.ToastManager
import com.style.http.exception.HttpResultException
import com.style.http.exception.HttpThrowableUtil
import io.reactivex.functions.Consumer

class HttpExceptionConsumer : Consumer<Throwable> {
    override fun accept(e: Throwable) {
        val t = HttpThrowableUtil.handleHttpError(e)
        when {
            HttpResultException.TOKEN_INVALID == t.errorCode -> onTokenError(t)
            HttpResultException.NETWORK_ERROR == t.errorCode -> onNetworkError(t)
            else -> onOtherError(t)
        }
    }

    private fun onOtherError(t: HttpResultException) {
        showToast(t.msg)
    }

    open fun onNetworkError(t: HttpResultException) {
        showToast(t.msg)
    }

    open fun onTokenError(t: HttpResultException) {
        showToast(t.msg)
        //MyApp.onTokenError()
    }

    private fun showToast(str: CharSequence) {
        ToastManager.showToast(AppManager.getInstance().getContext(), str)
    }
}