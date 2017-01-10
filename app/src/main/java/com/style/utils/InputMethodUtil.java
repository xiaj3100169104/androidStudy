package com.style.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 乱键盘处理工具类
 * Created by xiajun on 2017/1/9.
 */

public class InputMethodUtil {
    private static final String TAG = "CommonUtil";

    public static boolean showSoftInput(Activity activity, EditText editText) {
        boolean isFocus = editText.requestFocus();
        Log.e(TAG, "isFocus==" + isFocus);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isShow = imm.showSoftInput(editText, 0);
        Log.e(TAG, "isShow==" + isShow);
        if (!isShow)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        return isShow;
    }

    public static void hiddenSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            boolean isHide = imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            Log.e(TAG, "isHide==" + isHide);
        }
    }

    public static void hiddenSoftInput(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isHide = imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        Log.e(TAG, "isHide==" + isHide);
    }

    public static void toggleSoftInput(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            return;
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        } else {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
        }
    }
}
