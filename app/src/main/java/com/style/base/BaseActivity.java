package com.style.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.style.dialog.LoadingDialog;
import com.style.framework.R;
import com.style.manager.LogManager;
import com.style.manager.ToastManager;
import com.style.net.core2.RetrofitManager;
import com.style.rxAndroid.RXTaskManager;
import com.style.utils.CommonUtil;
import com.style.utils.DeviceInfoUtil;


public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    protected Context context;
    protected LayoutInflater mInflater;
    protected View mContentView;
    private LoadingDialog progressDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        context = this;
        mInflater = LayoutInflater.from(context);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //竖屏
        customWindowOptions(getWindow());

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
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置状态栏颜色
            //window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            //预留状态栏空间
            //mContentView.setFitsSystemWindows(true);
        }
    }

    //定义4.4窗口属性
    protected void customWindowKitkat(Window window) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //去掉状态栏
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //mContentView.setFitsSystemWindows(false);
        }
    }

    protected boolean isWrapContentView() {
        return true;
    }

    @Override
    public void setContentView(View mContentView) {
        if (isWrapContentView()) {
            /*不用CoordinatorLayout，主题里面的状态栏颜色不生效。
            v-21主题里面设置了状态栏透明，主题里面又能设置状态栏颜色；
            说明状态栏在5.0后activity根布局是CoordinatorLayout时状态栏是一个悬浮着的view。
            */
            ViewGroup rootView = new CoordinatorLayout(this);
            CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //一个view不能同时有两个父布局
            ((ViewGroup) mContentView.getParent()).removeView(mContentView);
            rootView.addView(mContentView, layoutParams);
            rootView.setFitsSystemWindows(true);
            mContentView = rootView;
            this.mContentView = mContentView;
        }
        super.setContentView(mContentView);
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
