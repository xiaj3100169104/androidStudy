package com.style.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 使用show/hide方式切换fragment真正可见时再刷新view。
 */
public abstract class BaseNoPagerLazyRefreshFragment extends BaseFragment {

    private boolean isViewCreated = false;
    private boolean isViewFirstVisible = false;

    /**
     * 注意：初始化添加fragment时调用hide方法，否则第一次fragment调show时不会调用此方法，
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isViewCreated && !hidden && !isViewFirstVisible) {
            onViewIsFirstVisible();
            isViewFirstVisible = true;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isViewCreated && !isHidden() && !isViewFirstVisible) {
            onViewIsFirstVisible();
            isViewFirstVisible = true;
        }
    }

    protected void onViewIsFirstVisible() {
    }
}
