package com.style.base

import android.os.Build
import android.view.View
import android.view.WindowManager

abstract class BaseDefaultTitleBarActivity : BaseTitleBarActivity() {

    override fun setContentView(contentView: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.decorView.systemUiVisibility = visibility
            window.statusBarColor = resources.getColor(android.R.color.transparent)
            contentView.fitsSystemWindows = false
        } else {
            contentView.fitsSystemWindows = true
        }
        super.setContentView(contentView)
    }

    override fun initTitleBar(mContentView: View) {
        super.initTitleBar(mContentView)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTransparentStatusBarHeight(getStatusHeight())
        } else {
            setTransparentStatusBarHeight(0)
        }
    }
}