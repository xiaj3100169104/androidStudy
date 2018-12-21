package com.style.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.ContextThemeWrapper;

import com.style.utils.DeviceInfoUtil;
import com.style.view.systemHelper.SmartToast;

/**
 * Created by xiajun on 2018/6/8.
 */

public abstract class BaseDialog extends Dialog {
    private SmartToast mToast;

    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    //调用show才会执行，所以在此之前获取控件为null
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected Activity getActivity() {
        return (Activity) ((ContextThemeWrapper) getContext()).getBaseContext();
    }

    protected int getScreenWidth() {
        return DeviceInfoUtil.getScreenWidth(getContext());
    }

    protected int getDefaultDialogWidth() {
        return (int) (getScreenWidth() * 0.8);
    }

    @Override
    public void dismiss() {
        cancelToast();
        super.dismiss();
    }

    private void cancelToast() {
        if (mToast != null)
            mToast.cancel();
    }

    public void onError(@StringRes int resId) {
        onError(getActivity().getString(resId));
    }

    public void onError(String message) {
        if (mToast == null)
            mToast = new SmartToast(getActivity());
        mToast.setType(SmartToast.ERROR);
        mToast.setMessage(message);
        mToast.show();
    }
}
