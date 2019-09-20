package com.style.base.activity

import android.view.View
import com.style.framework.R


/**
 * 一般标题栏样式，状态栏为自定义颜色，状态栏字体颜色风格是否为深色(6.0以上支持)
 */
abstract class BaseDefaultTitleBarActivity : BaseTitleBarActivity() {

    override fun setContentView(contentView: View) {
        super.setContentView(contentView)
        contentView.fitsSystemWindows = true
        setDarkMode(false, resources.getColor(R.color.colorPrimary))
        setStatusBarViewVisibility(false)
    }


}