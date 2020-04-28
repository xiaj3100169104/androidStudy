package com.style.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import com.style.toast.ToastManager
import com.style.utils.DeviceInfoUtil
import com.style.utils.LogManager
import org.simple.eventbus.EventBus

abstract class BaseFragment : Fragment() {
    protected var TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    //获取状态栏高度(竖屏时),有的手机竖屏时状态栏高度可能比较高
    open fun getStatusHeight(): Int {
        val statusBarHeight: Int = DeviceInfoUtil.getStatusHeight(context!!)
        return statusBarHeight
    }

    protected fun skip(cls: Class<*>) {
        if (isAdded)
            startActivity(Intent(context, cls))
    }

    fun logE(tag: String, msg: String) {
        LogManager.logE(tag, msg)
    }

    fun showToast(str: CharSequence) {
        if (isAdded)
            ToastManager.showToast(context, str)
    }

    fun showToast(@StringRes resId: Int) {
        if (isAdded)
            ToastManager.showToast(context, resId)
    }
}
