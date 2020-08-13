package com.style.base

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.style.lib.common.R


abstract class BaseTitleBarActivity : BaseActivity() {

    lateinit var layoutRoot: LinearLayout
    lateinit var statusBar: View
    lateinit var layoutTitle: RelativeLayout
    lateinit var viewBack: ImageView
    lateinit var tvTitle: TextView

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

    fun setTitleBarTitle(title: String) {
        tvTitle.text = title
    }

    fun setTitleBarTitle(@StringRes resId: Int) {
        tvTitle.text = getContext().getString(resId)
    }

    fun addRightMenu(text: String, whiteText: Boolean): TextView {
        val menu: TextView = layoutInflater.inflate(R.layout.title_bar_menu_single_text, null) as TextView
        menu.text = text
        if (whiteText)
            menu.setTextColor(Color.WHITE)
        else
            menu.setTextColor(Color.GRAY)
        val mlp = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(40f))
        mlp.marginEnd = dp2px(6f)
        val lp = RelativeLayout.LayoutParams(mlp)
        lp.addRule(RelativeLayout.ALIGN_PARENT_END)
        lp.addRule(RelativeLayout.CENTER_VERTICAL)
        layoutTitle.addView(menu, lp)
        menu.setOnClickListener {
            onClickTitleOption()
        }
        return menu
    }

    open fun onClickTitleOption() {

    }
}
