package com.style.base.activity

import android.os.Build
import android.view.View
import com.style.framework.R

/**
 * 一般样式标题栏，状态栏区域可用并且状态栏背景色为透明,状态栏字体颜色是否为深色
 */
abstract class BaseFullScreenStableTitleBarActivity : BaseTitleBarActivity() {

    private lateinit var statusBar: View

    open fun isLightStatusBar(): Boolean {
        return false
    }

    override fun setContentView(contentView: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            if (isLightStatusBar() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                visibility = visibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
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
        statusBar = mContentView.findViewById(R.id.status_bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusBar.layoutParams.height = getStatusHeight()
        } else {
            statusBar.layoutParams.height = 0
        }
    }
}