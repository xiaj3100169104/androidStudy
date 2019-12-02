package com.style.toast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.style.lib.common.R;
import com.style.utils.DeviceInfoUtil;

/**
 * Created by xiajun on 2018/6/15.
 */

public class SmartToast extends Toast {
    public static final int SUCCESS = 1;
    public static final int INFO = 0;
    public static final int ERROR = 2;
    private TextView messageTx;
    private ImageView icon;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public SmartToast(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.smart_toast, null);
        icon = root.findViewById(R.id.smart_toast_icon);
        messageTx = root.findViewById(R.id.smart_toast_message);
        //参照物，用来填充宽高
        View anchor = root.findViewById(R.id.anchor);
        anchor.getLayoutParams().width = DeviceInfoUtil.getDisplayMetrics(context).widthPixels;
        anchor.getLayoutParams().height = DeviceInfoUtil.dp2px(context, 48);
        setView(root);
        setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, DeviceInfoUtil.dp2px(context, 48));
        setDuration(Toast.LENGTH_SHORT);
    }

    public void setType(int type) {
        if (type == SUCCESS)
            icon.setImageResource(R.drawable.ic_svstatus_success);
        else if (type == ERROR)
            icon.setImageResource(R.drawable.ic_svstatus_error);
        else
            icon.setImageResource(R.drawable.ic_svstatus_info);

    }

    public void setMessage(String message) {
        messageTx.setText(message);
    }

    @Override
    public void show() {
        super.show();
    }
}
