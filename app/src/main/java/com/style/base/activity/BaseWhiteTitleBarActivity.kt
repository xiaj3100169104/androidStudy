package com.style.base.activity

import com.style.framework.R

/**
 * 一般标题栏样式，状态栏颜色为白色，状态栏字体颜色风格为深色(6.0以上支持)
 */
abstract class BaseWhiteTitleBarActivity : BaseDefaultTitleBarActivity() {


    override fun getStatusBarColor(): Int {
        return resources.getColor(R.color.white)
    }

    override fun isLightStatusBar(): Boolean {
        return true
    }
}