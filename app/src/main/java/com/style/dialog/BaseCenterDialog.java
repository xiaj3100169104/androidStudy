package com.style.dialog;

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

public abstract class BaseCenterDialog extends Dialog {
    private TextView tv_title;

    private TextView btnSure;
    private TextView btnCancel;


    public BaseCenterDialog(Context context) {
        super(context, R.style.Dialog_General);
    }

    public BaseCenterDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseCenterDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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

        btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
        btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);
        tv_title = (TextView) findViewById(R.id.tv_share_title);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSure();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCancel();
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

    public void setMyTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
    }

    protected abstract void init();
}
