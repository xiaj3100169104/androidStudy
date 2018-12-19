package com.style.base

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.View
import com.style.app.LogManager
import com.style.app.ToastManager
import org.simple.eventbus.EventBus

abstract class BaseFragment : Fragment() {
    protected var TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    fun <T : ViewDataBinding> getBinding(view: View): T {
        return DataBindingUtil.bind(view)!!
    }

    fun <T : ViewModel> getViewModel(modelClass: Class<T>): T {
        return ViewModelProviders.of(this).get(modelClass)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
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
