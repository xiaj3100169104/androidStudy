package com.style.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.style.framework.R;

/**
 * Created by xiajun on 2017/1/5.
 */

public abstract class BasePopupWindow extends PopupWindow {
    private Context mContext;
    public LayoutInflater mInflater;
    protected Integer mLayoutResID;
    protected View mContentView;

    public BasePopupWindow(Context context, ViewGroup rootView, int resId) {
        super();
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mContentView = inflater.inflate(resId, rootView, false);
        setContentView(mContentView);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_popupwindow_common));
        //this(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
    }

    public BasePopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }
}
