package com.style.base.activity

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

    private lateinit var tvTitleBase: TextView
    private lateinit var titleBar: RelativeLayout

    override fun setContentView(contentView: View) {
        super.setContentView(contentView)
        initTitleBar(contentView)
    }

    open fun initTitleBar(mContentView: View) {
        titleBar = mContentView.findViewById(R.id.title_bar)
        val ivBaseToolbarReturn = mContentView.findViewById<ImageView>(R.id.iv_base_toolbar_Return)
        tvTitleBase = mContentView.findViewById(R.id.tv_base_toolbar_title)
        ivBaseToolbarReturn.setOnClickListener { v -> onClickTitleBack() }
    }

    fun setTitleBarColor(@ColorInt color: Int) {
        titleBar.setBackgroundColor(color)
    }

    open fun onClickTitleBack() {
        onBackPressed()
    }

    fun setToolbarTitle(title: String) {
        tvTitleBase.text = title
    }

    fun setToolbarTitle(@StringRes resId: Int) {
        tvTitleBase.text = getContext().getString(resId)
    }

    fun addRightTextMenu(@StringRes textRes: Int, @ColorRes colorRes: Int, onClickListener: View.OnClickListener): TextView {
        val tv = TextView(getContext())
        tv.text = resources.getString(textRes)
        tv.setTextColor(resources.getColor(colorRes))
        val flowTextSize = DeviceInfoUtil.sp2px(getContext(), 17)
        tv.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, flowTextSize)
        tv.setSingleLine(true)
        tv.ellipsize = TextUtils.TruncateAt.END
        tv.maxWidth = dp2px(80f)
        tv.gravity = Gravity.CENTER
        tv.setOnClickListener(onClickListener)
        val lp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        lp.addRule(RelativeLayout.ALIGN_PARENT_END)
        addRightMenu(tv, lp)
        return tv
    }

    private fun addRightMenu(menu: View, lp: RelativeLayout.LayoutParams) {
        titleBar.addView(menu, lp)
    }


}
