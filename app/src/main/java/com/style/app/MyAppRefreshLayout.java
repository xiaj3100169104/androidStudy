package com.style.app;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.style.framework.R;

import example.helper.DynamicTimeFormat;
import example.home.SimpleRefreshHeader;

public class MyAppRefreshLayout extends SmartRefreshLayout {
    public static void init() {
        // 指定全局的下拉Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.pull_refresh_bg, R.color.pull_refresh_text);//全局设置主题颜色
                return new SimpleRefreshHeader(context);
            }
        });

        // 指定全局的上拉Footer
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(context);
            }
        });
    }

    public MyAppRefreshLayout(Context context) {
        super(context);
        initView();
    }

    public MyAppRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyAppRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyAppRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        setEnableFooterFollowWhenLoadFinished(true);

    }

    // 下拉/上拉完成
    public void complete() {
        if (mState == RefreshState.Loading) {
            finishLoadMore();
        } else {
            finishRefresh();
        }
    }

}
