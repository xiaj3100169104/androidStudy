package com.style.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dmcbig.mediapicker.utils.ScreenUtils;
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

    protected abstract BaseActivityPresenter getPresenter();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        context = this;
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
        if (isScreenPortrait())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //竖屏
        contentView = getLayoutInflater().inflate(getLayoutResId(), null);
        setContentView(getContentView());

    }

    public View getContentView() {
        return contentView;
    }

    public <T extends ViewDataBinding> T getBinding() {
        return DataBindingUtil.bind(getContentView());
    }

    @Override
    public void setContentView(View mContentView) {
        if (isGeneralTitleBar()) {
            customTitleOptions(mContentView);
        }
        Window window = getWindow();
        switch (getStatusBarStyle()) {
            case STATUS_BAR_TRANSPARENT:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setTransparentStatusBarHeight(getStatusHeight());
                    //防止之前加了这个标志
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isLightStatusBar())
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    else
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                } else {
                    setTransparentStatusBarHeight(0);
                }
                mContentView.setFitsSystemWindows(false);
                break;
            case STATUS_BAR_TRANSLUCENT:
                setTransparentStatusBarHeight(getStatusHeight());
                //看注释这个其实是在全透明的基础上多加了个半透明效果
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                mContentView.setFitsSystemWindows(false);
                break;
            case STATUS_BAR_COLOR:
                setTransparentStatusBarHeight(0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.orange));
                }
                mContentView.setFitsSystemWindows(true);
                break;
            case STATUS_BAR_THEME:
                setTransparentStatusBarHeight(0);
                mContentView.setFitsSystemWindows(true);
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
        ivBaseToolbarReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleBack();
            }
        });
    }

    protected void setTransparentStatusBarHeight(int height) {
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelToast();
        hideKeyboard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
        if (getPresenter() != null)
            getPresenter().onDestroy();
    }

    public Context getContext() {
        return context;
    }

    public void skip(Class<?> cls) {
        startActivity(new Intent(getContext(), cls));
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


    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    protected void logI(String tag, String msg) {
        LogManager.logI(tag, msg);
    }

    protected void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }
}
