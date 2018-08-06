package com.style.base;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.style.app.LogManager;
import com.style.dialog.LoadingDialog;
import com.style.framework.R;
import com.style.utils.DeviceInfoUtil;
import com.style.utils.InputMethodUtil;


public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();

    protected static final int STATUS_BAR_TRANSPARENT = 0;//全透明状态栏
    protected static final int STATUS_BAR_TRANSLUCENT = 1;//半透明状态栏
    protected static final int STATUS_BAR_COLOR = 2;//自定义状态栏颜色
    protected static final int STATUS_BAR_THEME = 3;//主题配置状态栏颜色

    private Context context;
    private LoadingDialog progressDialog;
    private View contentView;
    private Toast toast;

    protected boolean isScreenPortrait() {
        return true;
    }

    protected int getStatusBarStyle() {
        return STATUS_BAR_TRANSPARENT;
    }

    public abstract int getLayoutResId();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        logI(TAG, "onCreate-------------");
        context = this;
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
        if (isScreenPortrait())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //竖屏
        contentView = getLayoutInflater().inflate(getLayoutResId(), null);
        setContentView(getContentView());

    }

    public Context getContext() {
        return context;
    }

    public View getContentView() {
        return contentView;
    }

    public <T extends ViewDataBinding> T getBinding() {
        return DataBindingUtil.bind(getContentView());
    }

    public <T extends ViewModel> T getViewModel(@NonNull Class<T> modelClass) {
        return ViewModelProviders.of(this).get(modelClass);
    }

    @Override
    public void setContentView(View mContentView) {
        if (isGeneralTitleBar()) {
            customTitleOptions(mContentView);
        }
        Window window = getWindow();
        switch (getStatusBarStyle()) {
            case STATUS_BAR_TRANSPARENT:
                //默认属性可不写
                mContentView.setFitsSystemWindows(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setTransparentStatusBarHeight(getStatusHeight());
                    //防止之前加了这个标志
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    //系统版本大于6.0且需要设置状态栏图标颜色为深色
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isLightStatusBar())
                        visibility = visibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    window.getDecorView().setSystemUiVisibility(visibility);
                    //设置状态栏颜色
                    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                } else {
                    setTransparentStatusBarHeight(0);
                }
                break;
            case STATUS_BAR_TRANSLUCENT:
                //默认属性可不写
                mContentView.setFitsSystemWindows(false);
                setTransparentStatusBarHeight(getStatusHeight());
                //看注释这个其实是在全透明的基础上多加了个半透明效果
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                break;
            case STATUS_BAR_COLOR:
                //这是前提
                mContentView.setFitsSystemWindows(true);
                setTransparentStatusBarHeight(0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.orange));
                }
                break;
            case STATUS_BAR_THEME:
                //这是前提
                mContentView.setFitsSystemWindows(true);
                setTransparentStatusBarHeight(0);
                break;

        }
        super.setContentView(mContentView);
        initData();
    }

    //是否是一般标题栏布局
    protected boolean isGeneralTitleBar() {
        return true;
    }

    //是否是亮色状态栏
    protected boolean isLightStatusBar() {
        return false;
    }

    protected abstract void initData();

    private View statusBar;
    private TextView tvTitleBase;

    protected void customTitleOptions(View mContentView) {
        statusBar = mContentView.findViewById(R.id.status_bar);
        ImageView ivBaseToolbarReturn = mContentView.findViewById(R.id.iv_base_toolbar_Return);
        tvTitleBase = mContentView.findViewById(R.id.tv_base_toolbar_title);
        ivBaseToolbarReturn.setOnClickListener(v -> onClickTitleBack());
    }

    protected void setTransparentStatusBarHeight(int height) {
        if (isGeneralTitleBar())
            statusBar.getLayoutParams().height = height;

    }

    protected void onClickTitleBack() {
        onBackPressed();
    }

    protected void setToolbarTitle(String title) {
        tvTitleBase.setText(title);
    }

    protected void setToolbarTitle(@StringRes int resId) {
        tvTitleBase.setText(getContext().getString(resId));
    }

    @Override
    protected void onStart() {
        super.onStart();
        logI(TAG, "onStart-------------");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logI(TAG, "onRestart-------------");
    }

    /*
    //如果该activity位于栈底并且启动模式不是singleTask,finish会导致该activity销毁了又重建
     //finishAndRemoveTask()都不管用
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        logI(TAG, "onResume-------------");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logI(TAG, "onPause-------------");
        cancelToast();
        hideKeyboard();
    }

    @Override
    protected void onStop() {
        super.onStop();
        logI(TAG, "onStop-------------");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logI(TAG, "onDestroy-------------");
        dismissProgressDialog();
    }

    public void showToast(CharSequence str) {
        if (toast == null)
            toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.setText(str);
        toast.show();
    }

    public void showToast(@StringRes int resId) {
        showToast(getText(resId));
    }

    private void cancelToast() {
        if (toast != null)
            toast.cancel();
    }

    public void showProgressDialog() {
        showProgressDialog("");
    }

    public void showProgressDialog(@StringRes int msgId) {
        showProgressDialog(getText(msgId));
    }

    public void showProgressDialog(CharSequence msg) {
        if (progressDialog == null)
            progressDialog = new LoadingDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void hideKeyboard() {
        InputMethodUtil.hiddenSoftInput(this);

    }

    protected int dp2px(float dpValue) {
        return DeviceInfoUtil.dp2px(getContext(), dpValue);
    }

    //获取状态栏高度(竖屏时),有的手机竖屏时状态栏高度可能比较高
    protected int getStatusHeight() {
        int statusBarHeight = DeviceInfoUtil.getStatusHeight(this);
        return statusBarHeight;
    }

    protected void logI(String tag, String msg) {
        LogManager.logI(tag, msg);
    }

    protected void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }
}
