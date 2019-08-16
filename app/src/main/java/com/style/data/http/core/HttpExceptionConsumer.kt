package com.style.data.http.core

import com.style.app.AppManager
import com.style.app.MyApp
import com.style.data.http.exception.HttpThrowableUtil
import io.reactivex.functions.Consumer

class HttpExceptionConsumer : Consumer<Throwable> {
    override fun accept(e: Throwable) {
        val t = HttpThrowableUtil.handleHttpError(AppManager.getInstance().getContext(), e)
        showToast("")

    }

    open fun onNetworkError() {

    }

    open fun onTokenError() {
        //LApplication.skip2login()
    }

    private fun showToast(str: CharSequence) {
        //ViewUtils.showToast(str)
    }
}