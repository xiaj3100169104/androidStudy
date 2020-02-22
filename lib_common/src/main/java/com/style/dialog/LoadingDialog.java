package com.style.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.style.base.BaseDialog;
import com.style.lib.common.R;

public class LoadingDialog extends BaseDialog {

    public LoadingDialog(Context context) {
        super(context, R.style.Dialog_General);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        Window window = getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        //wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wmlp.gravity = Gravity.CENTER;
        window.setAttributes(wmlp);
        //window.setWindowAnimations(R.style.Animations_SlideInFromRight_OutToLeft);
    }

    public void setMessage(CharSequence msg) {
        if (!TextUtils.isEmpty(msg)) {
            TextView textView = (TextView) this.findViewById(R.id.tv_loading_info);
            textView.setText(msg);
        }
    }
}
