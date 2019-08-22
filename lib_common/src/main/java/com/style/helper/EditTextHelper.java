package com.style.helper;

import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.style.lib.common.R;

/**
 * Created by xiajun on 2018/3/26.
 */

public class EditTextHelper {
    /**
     * 点击切换模式使密码可见
     *
     * @param v
     * @param text
     */
    public static void setTransformationOnClick(View v, final EditText text) {
        String digits = v.getContext().getString(R.string.digits_password);
        setTransformationOnClick(v, text, digits);
    }

    public static void setTransformationOnClick(View v, final EditText text, String input) {
        text.setKeyListener(DigitsKeyListener.getInstance(input));
        text.setTransformationMethod(PasswordTransformationMethod.getInstance());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    //显示密码
                    text.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    text.setSelection(text.getText().length());
                } else {
                    //隐藏密码
                    text.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    text.setSelection(text.getText().length());
                }
            }
        });
    }

    /**
     * Touch模式使密码可见
     *
     * @param v
     * @param text
     */
    public static void setTransformationOnTouch(View v, final EditText text) {
        text.setTransformationMethod(PasswordTransformationMethod.getInstance());
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //显示密码
                        text.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        text.setSelection(text.getText().length());

                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        //隐藏密码
                        text.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        text.setSelection(text.getText().length());
                    }
                    break;
                }
                return false;
            }
        });
    }
}
