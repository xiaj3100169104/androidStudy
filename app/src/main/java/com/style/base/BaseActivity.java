package com.style.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmcbig.mediapicker.utils.ScreenUtils;
import com.style.dialog.LoadingDialog;
import com.style.framework.R;
import com.style.app.LogManager;
import com.style.app.ToastManager;
import com.style.utils.CommonUtil;
import com.style.utils.DeviceInfoUtil;


public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    protected Context context;
    protected View mContentView;
    private LoadingDialog progressDialog;

    //是否是系统默认状态栏颜色
    protected boolean isDefaultStatusBar() {
        return false;
    }

    //是否是主题配置状态栏颜色
    protected boolean isThemeStatusBar() {
        return false;
    }

    //是否是半透明状态栏
    protected boolean isTranslucentStatusBar() {
        return false;
    }

    //是否是全透明状态栏颜色，默认全屏风格
    protected boolean isTransparentStatusBar() {
        return true;
    }

    //获取状态栏高度(竖屏时),有的手机竖屏时状态栏高度可能比较高
    protected int getStatusHeight() {
        int statusBarHeight = getStatusHeightDefault();
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Log.e(TAG, "状态栏-高度:" + statusBarHeight);
        return statusBarHeight;
    }

    //获取状态栏高度(一般情况下)
    protected int getStatusHeightDefault() {
        return ScreenUtils.dp2px(this, 24f);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        context = this;
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //竖屏

    }

    @Override
    public void setContentView(View mContentView) {
        Window window = getWindow();
        if (isTransparentStatusBar()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //防止之前加了这个标志
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            }
            mContentView.setFitsSystemWindows(false);
        } else if (isDefaultStatusBar()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mContentView.setFitsSystemWindows(true);
        } else if (isThemeStatusBar()) {
            mContentView.setFitsSystemWindows(true);
        } else if (isTranslucentStatusBar()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mContentView.setFitsSystemWindows(false);
        }
        super.setContentView(mContentView);
        customTitleOptions(mContentView);

        initData();
    }

    public abstract void initData();

    private Toolbar toolbar;
    private TextView tvTitleBase;
    private ImageView ivBaseToolbarReturn;

    protected void customTitleOptions(View mContentView) {
        toolbar = mContentView.findViewById(R.id.toolbar);
        ivBaseToolbarReturn = mContentView.findViewById(R.id.iv_base_toolbar_Return);
        tvTitleBase = mContentView.findViewById(R.id.tv_base_toolbar_title);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
        //隐藏Toolbar的标题
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        ivBaseToolbarReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleBack();
            }
        });
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public View getToolbarRightView() {
        return ivBaseToolbarReturn;
    }

    protected void onClickTitleBack() {
        onBackFinish();
    }

    protected void setNavigationIcon(@DrawableRes int resId) {
        toolbar.setNavigationIcon(getResources().getDrawable(resId));
    }

    protected void setToolbarTitle(String title) {
        tvTitleBase.setText(getNotNullText(title));
    }

    protected void setToolbarTitle(@StringRes int resId) {
        tvTitleBase.setText(getContext().getString(resId));
    }

    @Override
    public void onBackPressed() {
        onBackFinish();
    }

    protected void onBackFinish() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
    }

    public Context getContext() {
        return context;
    }

    protected void skip(Class<?> cls) {
        startActivity(new Intent(getContext(), cls));
    }

    public void showToast(String str) {
        ToastManager.showToast(getContext(), str);
    }

    public void showToast(@StringRes int resId) {
        ToastManager.showToast(getContext(), resId);
    }

    public void showProgressDialog() {
        showProgressDialog("");
    }

    public void showProgressDialog(@StringRes int msgId) {
        showProgressDialog(getString(msgId));
    }

    public void showProgressDialog(String msg) {
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

    public void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }

    protected CharSequence getNotNullText(CharSequence str) {
        return CommonUtil.getNotNullText(str);
    }

    protected int dp2px(float dpValue) {
        return DeviceInfoUtil.dp2px(getContext(), dpValue);
    }

}
