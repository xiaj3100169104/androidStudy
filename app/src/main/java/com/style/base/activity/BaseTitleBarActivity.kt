package com.style.base.activity

import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.style.framework.R
import com.style.utils.DeviceInfoUtil


abstract class BaseTitleBarActivity : BaseActivity() {

    private lateinit var layoutRoot: LinearLayout
    private lateinit var statusBar: View
    private lateinit var layoutTitle: RelativeLayout
    private lateinit var viewBack: ImageView
    private lateinit var tvTitle: TextView

    override fun setContentView(contentView: View) {
        super.setContentView(contentView)
        contentView.fitsSystemWindows = false
        setFullScreenStableDarkMode(false)
        initTitleBar(contentView)
    }

    open fun initTitleBar(mContentView: View) {
        layoutRoot = mContentView.findViewById(R.id.base_title_bar_layout_root)
        statusBar = mContentView.findViewById(R.id.base_title_bar_status_bar)
        layoutTitle = mContentView.findViewById(R.id.base_title_bar_layout_title)
        viewBack = mContentView.findViewById(R.id.base_title_bar_iv_back)
        tvTitle = mContentView.findViewById(R.id.base_title_bar_tv_title)
        viewBack.setOnClickListener { v -> onClickTitleBack() }
        statusBar.layoutParams.height = getStatusHeight()
    }

    open fun onClickTitleBack() {
        onBackPressed()
    }

    fun setTitleBarColor(@ColorInt color: Int) {
        layoutRoot.setBackgroundColor(color)
    }

    fun setTitleBarTitle(title: String) {
        tvTitle.text = title
    }

    fun setTitleBarTitle(@StringRes resId: Int) {
        tvTitle.text = getContext().getString(resId)
    }

    fun addRightMenu(menu: View) {
        val lp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        lp.addRule(RelativeLayout.ALIGN_PARENT_END)
        layoutTitle.addView(menu, lp)
    }


}
