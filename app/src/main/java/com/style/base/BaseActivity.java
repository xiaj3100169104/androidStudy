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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmcbig.mediapicker.utils.ScreenUtils;
import com.style.dialog.LoadingDialog;
import com.style.framework.R;
import com.style.app.LogManager;
import com.style.app.ToastManager;
import com.style.utils.CommonUtil;
import com.style.utils.DeviceInfoUtil;


public abstract class BaseActivity extends AppCompatActivity {
    protected static final int STATUS_BAR_TRANSPARENT = 0;//全透明状态栏
    protected static final int STATUS_BAR_TRANSLUCENT = 1;//半透明状态栏
    protected static final int STATUS_BAR_THEME = 2;//主题配置状态栏颜色
    protected static final int STATUS_BAR_DEFAULT = 3;//系统默认状态栏颜色
    protected String TAG = getClass().getSimpleName();
    protected Context context;
    private BaseActivityPresenter mPresenter;
    private LoadingDialog progressDialog;


    protected int getStatusBarStyle() {
        return STATUS_BAR_TRANSPARENT;
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

    protected abstract BaseActivityPresenter getPresenter();

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
        switch (getStatusBarStyle()) {
            case STATUS_BAR_TRANSPARENT:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //防止之前加了这个标志
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    if (isLightStatusBar())
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    else
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                }
                mContentView.setFitsSystemWindows(false);
                break;
            case STATUS_BAR_DEFAULT:
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                mContentView.setFitsSystemWindows(true);
                break;
            case STATUS_BAR_THEME:
                mContentView.setFitsSystemWindows(true);
                break;
            case STATUS_BAR_TRANSLUCENT:
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                mContentView.setFitsSystemWindows(false);
                break;
        }
        super.setContentView(mContentView);
        if (isGeneralTitleBar()) {
            customTitleOptions(mContentView);
        }

        mPresenter = getPresenter();
        initData();
    }

    //是否是一般标题栏布局
    protected boolean isGeneralTitleBar() {
        return true;
    }

    //是否是亮色状态栏
    public boolean isLightStatusBar() {
        return false;
    }

    public abstract void initData();

    private LinearLayout titlebar;
    private View statusBar;
    private TextView tvTitleBase;
    private ImageView ivBaseToolbarReturn;

    protected void customTitleOptions(View mContentView) {
        titlebar = mContentView.findViewById(R.id.title_bar);
        statusBar = mContentView.findViewById(R.id.status_bar);
        ivBaseToolbarReturn = mContentView.findViewById(R.id.iv_base_toolbar_Return);
        tvTitleBase = mContentView.findViewById(R.id.tv_base_toolbar_title);
        if (getStatusBarStyle() == STATUS_BAR_DEFAULT || getStatusBarStyle() == STATUS_BAR_THEME)
            statusBar.getLayoutParams().height = 0;
        else
            statusBar.getLayoutParams().height = getStatusHeight();
        ivBaseToolbarReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleBack();
            }
        });
    }

    protected void onClickTitleBack() {
        onBackPressed();
    }

    protected void setToolbarTitle(String title) {
        tvTitleBase.setText(getNotNullText(title));
    }

    protected void setToolbarTitle(@StringRes int resId) {
        tvTitleBase.setText(getContext().getString(resId));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
