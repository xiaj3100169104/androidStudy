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
        initData()
    }

    fun <T : ViewDataBinding> getBinding(): T {
        return DataBindingUtil.bind(contentView)!!
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
        (activity as BaseDefaultTitleBarActivity).showToast(str)
    }

    fun showToast(@StringRes resId: Int) {
        (activity as BaseDefaultTitleBarActivity).showToast(resId)
    }
}
