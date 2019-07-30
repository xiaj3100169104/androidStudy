package com.style.base.activity

import android.graphics.Color
import android.os.Build
import android.view.View
import com.style.framework.R

/**
 * 一般标题栏样式，状态栏自定义颜色，状态栏字体颜色风格是否为深色(6.0以上支持)
 */
abstract class BaseDefaultTitleBarActivity : BaseTitleBarActivity() {


    open fun getStatusBarColor(): Int {
        return resources.getColor(R.color.colorPrimary)
    }

    open fun isLightStatusBar(): Boolean {
        return false
    }

    override fun setContentView(contentView: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isLightStatusBar() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val visibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.decorView.systemUiVisibility = visibility
            }
            window.statusBarColor = getStatusBarColor()
        }
        contentView.fitsSystemWindows = true
        super.setContentView(contentView)
    }
}