package com.style.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.style.dialog.LoadingDialog;
import com.style.utils.DeviceInfoUtil;
import com.style.utils.InputMethodUtil;
import com.style.utils.LogManager;

/**
 * FirstActivity->onPause()
 * SecondActivity->onCreate()
 * SecondActivity->onStart()
 * SecondActivity->onResume()
 * FirstActivity->onStop()
 * 如果SecondActivity的主题是Dialog或Translucent时，FirstActivity会调用onPause()而不调用onStop()
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    private Context context;
    private LoadingDialog progressDialog;
    private Toast toast;

    protected String getTAG() {
        return TAG;
    }

    protected Context getContext() {
        return context;
    }

    /**
     * 重置字体缩放比例为默认，所以需要全部继承该基类。
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        Configuration config = newBase.getResources().getConfiguration();
        config.fontScale = 1.0f;   //1 设置默认字体大小的倍数
        //显示大小重置为默认，7.0以上支持
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            int defaultDpi = DisplayMetrics.DENSITY_DEVICE_STABLE;
            config.densityDpi = defaultDpi;
        }
        Context newContext = newBase.createConfigurationContext(config);
        super.attachBaseContext(newContext);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logI(TAG, "onCreate-------------");
        context = this;
        //bug:主题窗口背景为透明且target=26(8.0)以上且系统为8.0时不能设置屏幕方向，8.1已修复，在子类重写判断即可
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void setFullScreenStableDarkMode(boolean dark) {
        setFullScreenStableDarkMode(dark, getResources().getColor(android.R.color.transparent));
    }

    /**
     * 设置状态栏区域可用
     *
     * @param dark           状态栏字体是否为深色
     * @param statusBarColor 状态栏背景颜色
     */
    protected void setFullScreenStableDarkMode(boolean dark, @ColorInt int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusBarColor);
            int visibility = window.getDecorView().getSystemUiVisibility();
            visibility = visibility | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (dark)
                    visibility = visibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                else
                    visibility = visibility & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                window.getDecorView().setSystemUiVisibility(visibility);
            }
        }
    }

    /**
     * 如果该activity位于栈底并且启动模式不是singleTask,finish会导致该activity销毁了又重建，
     * finishAndRemoveTask()也没有用，
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        logI(TAG, "onPause-------------");
        InputMethodUtil.hiddenSoftInput(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logI(TAG, "onDestroy-------------");
        dismissProgressDialog();
    }

    protected void showToast(CharSequence str) {
        if (toast == null)
            toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.setText(str);
        toast.show();
    }

    protected void showToast(@StringRes int resId) {
        showToast(getString(resId));
    }

    protected void cancelToast() {
        toast.cancel();
    }

    protected LoadingDialog showProgressDialog(@StringRes int msgId) {
        return showProgressDialog(getString(msgId));
    }

    /**
     * 使用注意后面不能在主线程执行耗时操作，会被阻塞延迟显示
     *
     * @return
     */
    protected LoadingDialog showProgressDialog(CharSequence msg) {
        if (progressDialog == null)
            progressDialog = new LoadingDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setMessage(msg);
        return progressDialog;
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    protected int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    protected void logI(String tag, String msg) {
        LogManager.logI(tag, msg);
    }

    protected void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }

    //获取状态栏高度(竖屏时),有的手机竖屏时状态栏高度要高一些
    protected int getStatusHeight() {
        return DeviceInfoUtil.getStatusHeight(this);
    }
}
