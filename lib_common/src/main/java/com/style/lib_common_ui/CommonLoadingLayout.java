package com.style.lib_common_ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.style.lib.common.R;

/**
 * 界面通用数据加载状态布局
 * Created by xiajun on 2019/10/12.
 */

@SuppressLint("RestrictedApi")
public class CommonLoadingLayout extends RelativeLayout {

    private View mLoadingView;
    private View mEmptyView;
    private View mNetworkErrorView;
    private OnClickRetryListener mListener;
    private View mContentView;

    public CommonLoadingLayout(Context context) {
        super(context);
        initView(context);
    }

    public CommonLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CommonLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CommonLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        mLoadingView = LayoutInflater.from(context).inflate(R.layout.common_loading_layout_loading, null, false);
        mEmptyView = LayoutInflater.from(context).inflate(R.layout.common_loading_layout_empty, null, false);
        mNetworkErrorView = LayoutInflater.from(context).inflate(R.layout.common_loading_layout_network_error, null, false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1)
            throw new RuntimeException("有且只能有一个子view(mContentView)");
        mContentView = getChildAt(0);
    }

    public View setEmptyView(@LayoutRes int layoutResId){
        mEmptyView = LayoutInflater.from(getContext()).inflate(layoutResId, null, false);
        return mEmptyView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    protected ViewGroup.LayoutParams getDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void showLoading() {
        removeAllViews();
        addView(mLoadingView, getDefaultLayoutParams());
    }

    public void showContent() {
        removeAllViews();
        addView(mContentView, getDefaultLayoutParams());
    }

    public void showEmpty() {
        removeAllViews();
        addView(mEmptyView, getDefaultLayoutParams());
    }

    public void showNetworkError() {
        removeAllViews();
        addView(mNetworkErrorView, getDefaultLayoutParams());
        mNetworkErrorView.findViewById(R.id.common_loading_layout_tv_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                if (null != mListener)
                    mListener.onRetry();
            }
        });
    }

    public void setOnClickRetryListener(OnClickRetryListener l) {
        this.mListener = l;
    }

    public interface OnClickRetryListener {
        void onRetry();
    }
}
