package com.style.base

import android.os.Bundle
import android.view.View

/**
 * 用于在FragmentPagerAdapter中切换fragment真正可见时再刷新view，
 * FragmentStatePagerAdapter中可不用,因为实例本身会被销毁。
 */
abstract class BasePagerLazyRefreshFragment : BaseFragment() {

    private var isViewCreated: Boolean = false
    private var isFirstViewVisible = false

    /**
     * 注意：此方法回调跟他本身生命周期无关，默认加载第一个fragment可见时onViewCreated还没执行，这就坑了.
     * 因此这种情况下判断第几次显示就不准确了，目前只判断第一次可见。
     *
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isViewCreated && isVisibleToUser && !isFirstViewVisible) {
            onFirstViewVisible()
            isFirstViewVisible = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        if (isViewCreated && userVisibleHint && !isFirstViewVisible) {
            onFirstViewVisible()
            isFirstViewVisible = true
        }
    }

    open fun onFirstViewVisible() {
    }
}
