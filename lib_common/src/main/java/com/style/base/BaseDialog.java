package com.style.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.StringRes;
import android.view.ContextThemeWrapper;

import com.style.toast.SmartToast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected Activity getActivity() {
        return (Activity) ((ContextThemeWrapper) getContext()).getBaseContext();
    }

    protected int getScreenWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    protected int getDefaultWidth() {
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
