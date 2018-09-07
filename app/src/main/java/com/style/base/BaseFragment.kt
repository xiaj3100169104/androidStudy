package com.style.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.View

import com.style.app.LogManager

import org.simple.eventbus.EventBus

abstract class BaseFragment : Fragment() {
    protected var TAG = javaClass.simpleName

    private var isViewCreated: Boolean = false
    private var isInit: Boolean = false
    private var isVisible2: Boolean = false
    private var isLazyData: Boolean = false

    protected abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        init()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            isVisible2 = true
        }
        init()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            isVisible2 = true
        }
        init()
    }

    private fun init() {
        if (isViewCreated && !isInit) {
            initData()
            isInit = true
        }
        if (isViewCreated && isVisible && isInit && !isLazyData) {
            lazyData()
            isLazyData = true
        }
    }

    open fun lazyData() {

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    protected fun skip(cls: Class<*>) {
        startActivity(Intent(context, cls))
    }

    fun logE(tag: String, msg: String) {
        LogManager.logE(tag, msg)
    }

    interface OnGiveUpEditDialogListener {
        fun onPositiveButton()

        fun onNegativeButton()
    }

    fun showToast(str: CharSequence) {
        (activity as BaseActivity).showToast(str)
    }

    fun showToast(@StringRes resId: Int) {
        (activity as BaseActivity).showToast(resId)
    }
}
