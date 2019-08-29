package com.style.data.http.exception

import com.style.data.app.AppManager
import com.style.app.ToastManager
import com.style.http.exception.HttpResultException
import com.style.http.exception.HttpThrowableUtil
import io.reactivex.functions.Consumer

class HttpExceptionConsumer : Consumer<Throwable> {
    override fun accept(e: Throwable) {
        val t = HttpThrowableUtil.handleHttpError(e)
        showToast(t.msg)
        when {
            HttpResultException.TOKEN_INVALID == t.errorCode -> onTokenError()
            HttpResultException.NETWORK_ERROR == t.errorCode -> onNetworkError()
            else -> onOtherError(t)
        }
    }

    private fun onOtherError(t: HttpResultException?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    open fun onNetworkError() {

    }

    open fun onTokenError() {
        //LApplication.skip2login()
    }

    private fun showToast(str: CharSequence) {
        ToastManager.showToast(AppManager.getInstance().getContext(), str)
    }
}