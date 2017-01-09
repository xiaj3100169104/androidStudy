package com.style.utils;

import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 编辑框处理工具类
 * Created by xiajun on 2017/1/9.
 */
public class EditTextUtil {

    public static void setInputTypeNumber(EditText text) {
        // 限制键盘只能输入字母和数字
        text.setKeyListener(new NumberKeyListener() {
            @Override
            public int getInputType() {
                // 0无键盘 1英文键盘 2模拟键盘 3数字键盘
                return InputType.TYPE_CLASS_NUMBER;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] mychar = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
                return mychar;
            }
        });
    }

    public static void setInputTypeEmail(EditText text) {
        // 限制键盘只能输入字母和数字
        text.setKeyListener(new NumberKeyListener() {
            @Override
            public int getInputType() {
                // 0无键盘 1英文键盘 2模拟键盘 3数字键盘
                return InputType.TYPE_CLASS_NUMBER;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] mychar = {'@', '.', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'g', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                        'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'G', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
                return mychar;
            }
        });
    }

    public static void setEdittextInputTypePassword(EditText text) {
        // 限制键盘只能输入字母和数字
        text.setKeyListener(new NumberKeyListener() {
            @Override
            public int getInputType() {
                // 0无键盘 1英文键盘 2模拟键盘 3数字键盘
                return InputType.TYPE_CLASS_TEXT;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] mychar = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'g', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                        'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'G', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
                return mychar;
            }
        });
    }

    public static void setEditTextTheLeastLengthListener(EditText et, final Button bt, final int lessLength) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int index, int start, int end) {
            }

            @Override
            public void beforeTextChanged(CharSequence cs, int index, int start, int end) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.length() >= lessLength) {
                    bt.setEnabled(true);
                } else {
                    bt.setEnabled(false);
                }
            }
        });
    }

    public static void setEditTextPhoneListener(EditText et, final Button bt) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int index, int start, int end) {
            }

            @Override
            public void beforeTextChanged(CharSequence cs, int index, int start, int end) {
            }

            @Override
            public void afterTextChanged(Editable phone) {
                if (FormatUtil.isMobileNum(String.valueOf(phone))) {
                    bt.setEnabled(true);
                } else {
                    bt.setEnabled(false);
                }
            }
        });
    }

    public static void setPasswordVisible(EditText et_password, View view) {
        // TODO Auto-generated method stub
        boolean isVisible = view.isSelected();
        view.setSelected(!isVisible);
        if (isVisible)
            // 文本正常显示
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            // 文本以密码形式显示
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // 下面两行代码实现: 输入框光标一直在输入文本后面
        Editable etable = et_password.getText();
        Selection.setSelection(etable, etable.length());
    }
}
