package com.style.base;

import android.content.Intent;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.style.toast.ToastManager;
import com.style.utils.DeviceInfoUtil;
import com.style.utils.LogManager;

public abstract class BaseFragment extends Fragment {

    protected String TAG = this.getClass().getSimpleName();

    protected int getStatusHeight() {
        return DeviceInfoUtil.getStatusHeight(requireContext());
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
