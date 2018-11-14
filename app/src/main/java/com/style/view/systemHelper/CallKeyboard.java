package com.style.view.systemHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.style.framework.R;

public class CallKeyboard extends LinearLayout {
    private Context mContext;
    private OnNumberClickListener listener;
    private String[] data = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "#", "0", "*"};

    public CallKeyboard(Context context) {
        super(context);
        initView(context);
    }

    public CallKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {

        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.call_keyboard, null);
        GridLayout gridLayout = (GridLayout) view.findViewById(R.id.gridlayout);
        for (int i = 0; i < 12; i++) {
            final int n = i;
            View bt = gridLayout.getChildAt(i);
            bt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value = data[n];
                    if (listener != null)
                        listener.onClickNumber(value);
                }
            });
        }
        this.addView(view);
    }

    public void setOnNumberClickListener(OnNumberClickListener listener) {
        if (listener != null)
            this.listener = listener;
    }

    public interface OnNumberClickListener {
        void onClickNumber(String s);
    }
}
