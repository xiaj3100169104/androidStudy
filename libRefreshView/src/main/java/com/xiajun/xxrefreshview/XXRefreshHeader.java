package com.xiajun.xxrefreshview;

/**
 * Created by xiajun on 2018/10/24.
 */

public interface XXRefreshHeader {
    /**
     * ({@link com.xiajun.xxrefreshview.XXRefreshView#setState(int)}).
     * * @param state 刷新布局当前状态
     */
    void onState(int state);

    /**
     * @param heightSlop 触发刷新可见高度
     * @param total      本身高度
     */
    void initHeight(int heightSlop, int total);

    /**
     * 刷新头部可见高度变化时回调
     *
     * @param height 可见高度
     */
    void onVisibleHeightChanged(int height);

}
