package com.style.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.style.base.BaseDialog;
import com.style.framework.R;
import com.style.utils.DeviceInfoUtil;

public class SelAvatarDialog extends BaseDialog {

    private Button bt_takePhoto;
    private Button bt_selPhoto;
    private OnItemClickListener mListener;
    private Button bt_selCancel;

    public SelAvatarDialog(Context context) {
        super(context, R.style.Dialog_General);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_sel_avatar);
        Window window = getWindow();
        //默认对话框会有边距，宽度不能占满屏幕
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setWindowAnimations(R.style.Animations_SlideInFromBottom_OutToBottom);
        bt_takePhoto = (Button) this.findViewById(R.id.item_camera);
        bt_selPhoto = (Button) this.findViewById(R.id.item_photo);
        bt_selCancel = (Button) this.findViewById(R.id.item_cancel);
        bt_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.OnClickCamera();
                dismiss();
            }
        });
        bt_selPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.OnClickPhoto();
                dismiss();
            }
        });
        bt_selCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.OnClickCancel();
                dismiss();
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        if (mListener != null)
            this.mListener = mListener;
    }

    public interface OnItemClickListener {
        void OnClickCamera();

        void OnClickPhoto();

        void OnClickCancel();
    }
}
