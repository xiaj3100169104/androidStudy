package com.style.manager;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.style.framework.R;

import example.app.MyApp;


public class ToastManager {

    public static void showToastNoMoreData(Context context) {
        showToast(context, R.string.no_more);
    }

    public static void showToast(Context context, String str) {
        if (!TextUtils.isEmpty(str) && null != context) {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(Context context, int resId) {
        if (null != context) {
            Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToastOnApplication(int resId) {
        showToast(MyApp.getAppContext(), resId);
    }

    public static void showToastOnApplication(String str) {
       /* Toast toast = new Toast(MyApp.getAppContext());
        toast.setGravity(Gravity.TOP, 0, 126);
        LinearLayout layout = (LinearLayout) LayoutInflater.from(MyApp.getAppContext()).inflate(R.layout.smart_toast, null);
        ImageView icon = (ImageView) layout.findViewById(R.id.smart_toast_icon);
        icon.setImageResource(R.drawable.ic_action_info);
        TextView messageTx = (TextView) layout.findViewById(R.id.smart_toast_message);
        messageTx.setText(str);
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();*/
        showToast(MyApp.getAppContext(), str);
    }

    public static void showToastonFailureOnApp() {
        showToastOnApplication(R.string.request_fail);
    }

    public static void showToastRequestFail(Context context) {
        showToast(context, R.string.request_fail);
    }

    public static void showToastLong(Context context, String msg) {

        if (!TextUtils.isEmpty(msg) && null != context) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    public static void showToastLong(Context context, int resId) {
        if (null != context) {
            Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
        }
    }
}
