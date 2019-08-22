package com.style.view.refresh;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.xiajun.xxrefreshview.R;

public class MyAppRefreshLayout extends SmartRefreshLayout {
    public static void init() {
        // 指定全局的Header
        setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.pull_refresh_bg, R.color.pull_refresh_text);//全局设置主题颜色
            return new SimpleRefreshHeader(context);
        });
        // 指定全局的footer
        setDefaultRefreshFooterCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.pull_refresh_bg, R.color.pull_refresh_text);//全局设置主题颜色
            return new SimpleRefreshFooter(context);
        });

        // 默认刷新加载布局
        //setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context));
        //setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context));
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
