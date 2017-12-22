package com.style.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.style.dialog.LoadingDialog;
import com.style.framework.R;
import com.style.manager.LogManager;
import com.style.manager.ToastManager;
import com.style.net.core2.RetrofitManager;
import com.style.rxAndroid.RXTaskManager;
import com.style.utils.CommonUtil;
import com.style.utils.DeviceInfoUtil;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    protected Context context;
    protected LayoutInflater mInflater;
    protected Integer mLayoutResID;
    protected View mContentView;
    private LoadingDialog progressDialog;


    public <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        context = this;
        mInflater = LayoutInflater.from(context);
        if (mLayoutResID != null)
            mContentView = mInflater.inflate(mLayoutResID, null);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //竖屏
        customWindowOptions(getWindow());

        setContentView(mContentView);
        //ButterKnife must set after setContentView
        ButterKnife.bind(this);
        initData();
    }

    public abstract void initData();

    //自定义窗口属性
    protected void customWindowOptions(Window window) {
        //定义4.4以下窗口属性
        customWindowKitkat(window);
        //定义5.0以上窗口属性
        customWindowLollipop(window);
    }

    //定义5.0以上窗口属性
    protected void customWindowLollipop(Window window) {
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置状态栏颜色
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            //预留状态栏空间
            mContentView.setFitsSystemWindows(true);
        }
    }

    //定义4.4窗口属性
    protected void customWindowKitkat(Window window) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //去掉状态栏
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mContentView.setFitsSystemWindows(false);
        }
    }

    @Override
    public void setContentView(View mContentView) {
        customTitleOptions(mContentView);
        super.setContentView(mContentView);
    }

    protected void customTitleOptions(View mContentView) {
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
        RetrofitManager.getInstance().removeTask(TAG);
        RXTaskManager.getInstance().removeTask(TAG);
        ButterKnife.unbind(this);
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

    public void showToast(int resId) {
        ToastManager.showToast(getContext(), resId);
    }


    public void showToastLong(String msg) {
        ToastManager.showToastLong(getContext(), msg);
    }

    public void showToastLong(int msgId) {
        ToastManager.showToastLong(getContext(), msgId);
    }

    public void showProgressDialog() {
        showProgressDialog("");
    }

    public void showProgressDialog(int msgId) {
        showProgressDialog(getString(msgId));
    }

    public void showProgressDialog(String msg) {
        if (progressDialog == null)
            progressDialog = new LoadingDialog(getContext(), R.style.Dialog_NoTitle);
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
