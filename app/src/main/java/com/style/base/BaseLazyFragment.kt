package com.style.base

import android.os.Bundle
import android.view.View

abstract class BaseLazyFragment : BaseFragment() {

    private var isViewCreated: Boolean = false
    private var isInit: Boolean = false
    private var isVisible2: Boolean = false
    private var isLazyData: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        init()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            isVisible2 = true
        }
        init()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            isVisible2 = true
        }
        init()
    }

    private fun init() {
        if (isViewCreated && !isInit) {
            isInit = true
        }
        if (isViewCreated && isVisible && isInit && !isLazyData) {
            lazyData()
            isLazyData = true
        }
    }

    open fun lazyData() {

    }
}
