package com.style.base.activity

import android.view.View
import com.style.framework.R

/**
 * 一般标题栏样式，状态栏颜色为白色，状态栏字体颜色风格为深色(6.0以上支持)
 */
abstract class BaseWhiteTitleBarActivity : BaseTitleBarActivity() {

    override fun setContentView(contentView: View) {
        super.setContentView(contentView)
        contentView.fitsSystemWindows = true
        setDarkMode(true, resources.getColor(R.color.white))
        setStatusBarViewVisibility(false)
    }
}