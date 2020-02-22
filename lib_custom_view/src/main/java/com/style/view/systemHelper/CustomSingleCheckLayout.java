package com.style.view.systemHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 自定义单选中子view容器，状态改变用state_selected
 */
public class CustomSingleCheckLayout extends LinearLayout {

    private int count;
    private OnClickChildListener mOnClickChildListener;

    public CustomSingleCheckLayout(Context context) {
        this(context, null);
    }

    public CustomSingleCheckLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSingleCheckLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        count = getChildCount();
        for (int i = 0; i < count; i++) {
            int p = i;
            getChildAt(p).setOnClickListener(v -> {
                changeSelected(p);
                if (mOnClickChildListener != null) {
                    mOnClickChildListener.onClickChild(p);
                }
            });
        }
    }

    private void setChildAllChildSelect(View child, boolean b) {
        child.setSelected(b);
        if (child instanceof ViewGroup) {
            int c = ((ViewGroup) child).getChildCount();
            for (int k = 0; k < c; k++) {
                setChildAllChildSelect(((ViewGroup) child).getChildAt(k), b);
            }
        }
    }

    public void changeSelected(int position) {
        for (int j = 0; j < count; j++) {
            View child = getChildAt(j);
            if (j == position) {
                setChildAllChildSelect(child, true);
            } else {
                setChildAllChildSelect(child, false);
            }
        }
    }

    public void setOnClickChildListener(OnClickChildListener l) {
        this.mOnClickChildListener = l;
    }

    public interface OnClickChildListener {
        void onClickChild(int position);
    }
}
