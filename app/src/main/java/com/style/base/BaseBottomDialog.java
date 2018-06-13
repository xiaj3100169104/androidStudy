package com.style.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.style.framework.R;

/**
 * Created by xiajun on 2018/6/8.
 */

public abstract class BaseBottomDialog extends Dialog {
    private TextView btnSure;

    public BaseBottomDialog(Context context) {
        super(context, R.style.Dialog_General);
    }

    public BaseBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseBottomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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
        window.setGravity(Gravity.BOTTOM);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) ((ContextThemeWrapper) getContext()).getBaseContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout(dm.widthPixels, window.getAttributes().height);
        window.setWindowAnimations(R.style.Animations_SlideInFromBottom_OutToBottom);

        btnSure = (TextView) findViewById(R.id.btn_ok);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSure();
            }
        });

        init();
    }

    protected void onClickCancel() {
        dismiss();
    }

    protected void onClickSure() {
        dismiss();
    }

    protected abstract void init();
}
