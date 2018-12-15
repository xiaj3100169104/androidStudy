package com.style.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.os.Bundle
import android.support.annotation.Nullable
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
    private lateinit var context: Context
    private var progressDialog: LoadingDialog? = null
    private lateinit var mContentView: View
    private var toast: Toast? = null

    fun getContext(): Context {
        return context;
    }

    //获取状态栏高度(竖屏时),有的手机竖屏时状态栏高度可能比较高
    open fun getStatusHeight(): Int {
        val statusBarHeight: Int = DeviceInfoUtil.getStatusHeight(this)
        return statusBarHeight
    }

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        logI(TAG, "onCreate-------------")
        context = this
        //bug:主题窗口背景为透明且target=26(8.0)以上且系统为8.0时不能设置屏幕方向，8.1已修复，在子类重写判断即可
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun setContentView(layoutResID: Int) {
        mContentView = layoutInflater.inflate(layoutResID, null)
        setContentView(mContentView)
        initData()
    }

    fun getContentView(): View {
        return mContentView
    }

    fun <T : ViewDataBinding> getBinding(): T {
        return DataBindingUtil.bind(getContentView())!!
    }

    fun <T : ViewModel> getViewModel(modelClass: Class<T>): T {
        return ViewModelProviders.of(this).get(modelClass)
    }

    protected abstract fun initData()

    /**
    //如果该activity位于栈底并且启动模式不是singleTask,finish会导致该activity销毁了又重建
    //finishAndRemoveTask()都不管用
     */
    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        logI(TAG, "onPause-------------")
        cancelToast()
        hideKeyboard()
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
        showToast(getString(resId))
    }

    private fun cancelToast() {
        if (toast != null)
            toast!!.cancel()
    }

    fun showProgressDialog(@StringRes msgId: Int) {
        showProgressDialog(getString(msgId))
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

    fun dp2px(dpValue: Float): Int {
        return DeviceInfoUtil.dp2px(context, dpValue)
    }

    fun logI(tag: String, msg: String) {
        LogManager.logI(tag, msg)
    }

    fun logE(tag: String, msg: String) {
        LogManager.logE(tag, msg)
    }

    fun skip(cls: Class<*>) {
        startActivity(Intent(this, cls))
    }
}
