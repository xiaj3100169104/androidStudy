package com.style.base

import android.os.Bundle
import android.view.View

/**
 * 使用show/hide方式切换fragment真正可见时再刷新view。
 */
abstract class BaseNoPagerLazyRefreshFragment : BaseFragment() {

    private var isViewCreated: Boolean = false

    /**
     * 前提：在切换期间fragment没有被销毁。第几次可见，统计show了多少次，第一次显示可判断showCount == 1
     */
    private var showCount = 1

    /**
     * 注意：初始化添加fragment时调用hide方法，否则第一次fragment调show时不会调用此方法，
     *
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (isViewCreated && !hidden) {
            logE(TAG, "第${showCount}次显示")
            onViewVisible(showCount)
            showCount++
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
    }

    override fun onResume() {
        super.onResume()
        if (isViewCreated && !isHidden) {
            logE(TAG, "第${showCount}次显示")
            onViewVisible(showCount)
            showCount++
        }
    }

    open fun onViewVisible(showCount: Int) {
    }
}
