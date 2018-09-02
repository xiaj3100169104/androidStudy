package com.style.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.ActivityInfo
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.style.app.LogManager
import com.style.dialog.LoadingDialog
import com.style.framework.R
import com.style.utils.DeviceInfoUtil
import com.style.utils.InputMethodUtil


abstract class BaseActivity : AppCompatActivity() {
    protected val TAG = javaClass.simpleName

    lateinit var context: Context
    private var progressDialog: LoadingDialog? = null
    private var contentView: View? = null
    private var toast: Toast? = null

    open fun isScreenPortrait(): Boolean {
        return true
    }

    open fun getStatusBarStyle(): Int {
        return STATUS_BAR_TRANSPARENT
    }

    abstract fun getLayoutResId(): Int

    //是否是一般标题栏布局
    open fun isGeneralTitleBar(): Boolean {
        return true
    }

    //是否是亮色状态栏
    protected fun isLightStatusBar(): Boolean {
        return false
    }

    private var statusBar: View? = null
    private var tvTitleBase: TextView? = null

    //获取状态栏高度(竖屏时),有的手机竖屏时状态栏高度可能比较高
    open fun getStatusHeight(): Int {
        val statusBarHeight: Int = DeviceInfoUtil.getStatusHeight(this)
        return statusBarHeight
    }

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        logI(TAG, "onCreate-------------")
        context = this
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
        if (isScreenPortrait())
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT  //竖屏
        contentView = layoutInflater.inflate(getLayoutResId(), null)
        setContentView(getContentView())

    }

    fun getContentView(): View? {
        return contentView
    }

    fun <T : ViewDataBinding> getBinding(): T {
        return DataBindingUtil.bind(getContentView())
    }

    fun <T : ViewModel> getViewModel(modelClass: Class<T>): T {
        return ViewModelProviders.of(this).get(modelClass)
    }

    override fun setContentView(mContentView: View?) {
        if (isGeneralTitleBar()) {
            customTitleOptions(mContentView)
        }
        val window = window
        when (getStatusBarStyle()) {
            STATUS_BAR_TRANSPARENT -> {
                //默认属性可不写
                mContentView!!.fitsSystemWindows = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setTransparentStatusBarHeight(getStatusHeight())
                    //防止之前加了这个标志
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    var visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    //系统版本大于6.0且需要设置状态栏图标颜色为深色
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isLightStatusBar())
                        visibility = visibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    window.decorView.systemUiVisibility = visibility
                    //设置状态栏颜色
                    window.statusBarColor = resources.getColor(android.R.color.transparent)
                } else {
                    setTransparentStatusBarHeight(0)
                }
            }
            STATUS_BAR_TRANSLUCENT -> {
                //默认属性可不写
                mContentView!!.fitsSystemWindows = false
                setTransparentStatusBarHeight(getStatusHeight())
                //看注释这个其实是在全透明的基础上多加了个半透明效果
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
            STATUS_BAR_COLOR -> {
                //这是前提
                mContentView!!.fitsSystemWindows = true
                setTransparentStatusBarHeight(0)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = resources.getColor(R.color.orange)
                }
            }
            STATUS_BAR_THEME -> {
                //这是前提
                mContentView!!.fitsSystemWindows = true
                setTransparentStatusBarHeight(0)
            }
        }
        super.setContentView(mContentView)
        initData()
    }

    protected abstract fun initData()

    protected fun customTitleOptions(mContentView: View?) {
        statusBar = mContentView!!.findViewById(R.id.status_bar)
        val ivBaseToolbarReturn = mContentView.findViewById<ImageView>(R.id.iv_base_toolbar_Return)
        tvTitleBase = mContentView.findViewById(R.id.tv_base_toolbar_title)
        ivBaseToolbarReturn.setOnClickListener { v -> onClickTitleBack() }
    }

    protected fun setTransparentStatusBarHeight(height: Int) {
        if (isGeneralTitleBar())
            statusBar!!.layoutParams.height = height

    }

    protected fun onClickTitleBack() {
        onBackPressed()
    }

    protected fun setToolbarTitle(title: String) {
        tvTitleBase!!.text = title
    }

    protected fun setToolbarTitle(@StringRes resId: Int) {
        tvTitleBase!!.text = context!!.getString(resId)
    }

    override fun onStart() {
        super.onStart()
        logI(TAG, "onStart-------------")
    }

    override fun onRestart() {
        super.onRestart()
        logI(TAG, "onRestart-------------")
    }

    /*
    //如果该activity位于栈底并且启动模式不是singleTask,finish会导致该activity销毁了又重建
     //finishAndRemoveTask()都不管用
     */
    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        logI(TAG, "onResume-------------")
    }

    override fun onPause() {
        super.onPause()
        logI(TAG, "onPause-------------")
        cancelToast()
        hideKeyboard()
    }

    override fun onStop() {
        super.onStop()
        logI(TAG, "onStop-------------")
    }

    override fun onDestroy() {
        super.onDestroy()
        logI(TAG, "onDestroy-------------")
        dismissProgressDialog()
    }

    fun showToast(str: CharSequence) {
        if (toast == null)
            toast = Toast.makeText(context, str, Toast.LENGTH_SHORT)
        toast!!.setText(str)
        toast!!.show()
    }

    fun showToast(@StringRes resId: Int) {
        showToast(getText(resId))
    }

    private fun cancelToast() {
        if (toast != null)
            toast!!.cancel()
    }

    fun showProgressDialog(@StringRes msgId: Int) {
        showProgressDialog(getText(msgId))
    }

    @JvmOverloads
    fun showProgressDialog(msg: CharSequence = "") {
        if (progressDialog == null)
            progressDialog = LoadingDialog(context)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setMessage(msg)
        progressDialog!!.show()
    }

    fun dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    fun hideKeyboard() {
        InputMethodUtil.hiddenSoftInput(this)

    }

    protected fun dp2px(dpValue: Float): Int {
        return DeviceInfoUtil.dp2px(context, dpValue)
    }

    protected fun logI(tag: String, msg: String) {
        LogManager.logI(tag, msg)
    }

    protected fun logE(tag: String, msg: String) {
        LogManager.logE(tag, msg)
    }

    companion object {

        protected const val STATUS_BAR_TRANSPARENT = 0//全透明状态栏
        protected const val STATUS_BAR_TRANSLUCENT = 1//半透明状态栏
        protected const val STATUS_BAR_COLOR = 2//自定义状态栏颜色
        protected const val STATUS_BAR_THEME = 3//主题配置状态栏颜色
    }
}
