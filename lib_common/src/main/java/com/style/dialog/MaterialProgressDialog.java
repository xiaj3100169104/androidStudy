package com.style.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.WindowManager;
import android.widget.TextView;

import com.style.lib.common.R;


/**
 * Created by xiajun on 2018/6/8.
 */

public class MaterialProgressDialog extends Dialog {

    public MaterialProgressDialog(@NonNull Context context) {
        super(context, R.style.MaterialProgressDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setCancelable(true);
        setCanceledOnTouchOutside(true);*/
        setContentView(R.layout.progress_dialog_material);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void setContent(String s) {
        TextView tv = findViewById(R.id.tv_load_dialog_content);
        tv.setText(s);
    }
}
