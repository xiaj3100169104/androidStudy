package com.style.lib_common_ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.TextView;

import com.style.lib.common_ui.R;


/**
 * Created by xiajun on 2018/6/8.
 */

public class MaterialProgressDialog extends Dialog {

    public MaterialProgressDialog(@NonNull Context context) {
        super(context, R.style.MaterialProgressDialog);
    }

    public MaterialProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MaterialProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(getContext());
    }

    private void init(Context context) {
        /*setCancelable(true);
        setCanceledOnTouchOutside(true);*/
        setContentView(R.layout.progress_dialog_material);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show() {
        super.show();
    }

    public void setContent(String s) {
        TextView tv = findViewById(R.id.tv_load_dialog_content);
        tv.setText(s);
    }
}
