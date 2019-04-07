package com.style.base.activity

import android.os.Build
import android.view.View

/**
 * 如果有编辑框布局在界面底部时设置fitsSystemWindows = false，弹出键盘布局不会调整,
 * 此时可以用这个基类
 */
abstract class BaseHideStatusTitleBarActivity : BaseTitleBarActivity() {

    override fun setContentView(contentView: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(android.R.color.black)
        }
        contentView.fitsSystemWindows = true
        super.setContentView(contentView)
    }

    override fun initTitleBar(mContentView: View) {
        super.initTitleBar(mContentView)
        setTransparentStatusBarHeight(0)
    }
}