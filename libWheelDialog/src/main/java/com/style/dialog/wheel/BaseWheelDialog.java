package com.style.dialog.wheel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Window;

/**
 * Created by xiajun on 2018/6/8.
 */

public abstract class BaseWheelDialog extends Dialog {
    public BaseWheelDialog(Context context) {
        super(context);
    }

    public BaseWheelDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseWheelDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) ((ContextThemeWrapper) getContext()).getBaseContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout(Utils.dp2px(getContext(), 300), window.getAttributes().height);

        init();
    }

    protected abstract void init();
}
