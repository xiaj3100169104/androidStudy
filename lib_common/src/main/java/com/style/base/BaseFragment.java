package com.style.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.style.toast.ToastManager;
import com.style.utils.DeviceInfoUtil;
import com.style.utils.LogManager;

import org.simple.eventbus.EventBus;

public abstract class BaseFragment extends Fragment {
    protected String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //获取状态栏高度(竖屏时),有的手机竖屏时状态栏高度可能比较高
    protected int getStatusHeight() {
        int statusBarHeight = DeviceInfoUtil.getStatusHeight(getContext());
        return statusBarHeight;
    }

    protected void skip(Class<?> cls) {
        if (isAdded())
            startActivity(new Intent(getContext(), cls));
    }

    protected void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }

    protected void showToast(CharSequence str) {
        if (isAdded())
            ToastManager.showToast(getContext(), str);
    }

    protected void showToast(@StringRes int resId) {
        if (isAdded())
            ToastManager.showToast(getContext(), resId);
    }
}
