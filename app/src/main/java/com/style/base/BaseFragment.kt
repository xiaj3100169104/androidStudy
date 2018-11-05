package com.style.base

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.style.app.LogManager

import org.simple.eventbus.EventBus

abstract class BaseFragment : Fragment() {
    protected var TAG = javaClass.simpleName

    private var isViewCreated: Boolean = false
    private var isInit: Boolean = false
    private var isVisible2: Boolean = false
    private var isLazyData: Boolean = false

    protected abstract fun getLayoutResId(): Int
    private lateinit var contentView: View
    protected abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = inflater.inflate(getLayoutResId(), container, false)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        init()
    }

    fun <T : ViewDataBinding> getBinding(): T {
        return DataBindingUtil.bind(contentView)!!
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
        (activity as BaseTitleBarActivity).showToast(str)
    }

    fun showToast(@StringRes resId: Int) {
        (activity as BaseTitleBarActivity).showToast(resId)
    }
}
