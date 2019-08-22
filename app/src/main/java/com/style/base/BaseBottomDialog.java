package com.style.base;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.style.framework.R;

/**
 * Created by xiajun on 2018/6/8.
 */

public abstract class BaseBottomDialog extends BaseDialog {
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
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setLayout(getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setWindowAnimations(R.style.Animations_SlideInFromBottom_OutToBottom);
        window.setGravity(Gravity.BOTTOM);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        btnSure = (TextView) findViewById(R.id.btn_ok);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSure();
            }
        });
    }

    protected void onClickCancel() {
        dismiss();
    }

    protected void onClickSure() {
        dismiss();
    }

}
