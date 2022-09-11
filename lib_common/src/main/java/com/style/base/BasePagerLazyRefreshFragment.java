package com.style.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 用于在FragmentPagerAdapter中切换fragment真正可见时再刷新view，
 * FragmentStatePagerAdapter中可不用,因为实例本身会被销毁。
 */
public abstract class BasePagerLazyRefreshFragment extends BaseFragment {

    private boolean isViewCreated = false;
    private boolean isViewFirstVisible = false;

    /**
     * 注意：默认加载第一个fragment可见时onViewCreated还没执行，这里有坑.
     * 因此这种情况下判断第几次显示就不准确了，目前只判断第一次可见。
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isViewCreated && isVisibleToUser && !isViewFirstVisible) {
            onViewIsFirstVisible();
            isViewFirstVisible = true;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        if (isViewCreated && getUserVisibleHint() && !isViewFirstVisible) {
            onViewIsFirstVisible();
            isViewFirstVisible = true;
        }
    }

    protected void onViewIsFirstVisible() {
    }
}
