package com.style.base;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.style.framework.R;

/**
 * Created by xiajun on 2018/6/8.
 */

public abstract class BaseCenterTitleDialog extends BaseDialog {
    private TextView tv_title;
    private TextView btnSure;
    private TextView btnCancel;


    public BaseCenterTitleDialog(Context context) {
        super(context, R.style.Dialog_General);
    }

    public BaseCenterTitleDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseCenterTitleDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setLayout(getDefaultWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        btnSure = (TextView) findViewById(R.id.btn_ok);
        btnCancel = (TextView) findViewById(R.id.btn_cancel);
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
}
