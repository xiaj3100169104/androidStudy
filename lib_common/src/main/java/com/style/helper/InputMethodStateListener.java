package com.style.helper;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.style.utils.DeviceInfoUtil;

/**
 * Created by xiajun on 2018/7/13.
 * 布局可视区域监听器，可监听软键盘状态
 */

public abstract class InputMethodStateListener implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = "InputMethodState";

    final Rect rect = new Rect();
    private final int screenHeight;
    private final int keyHeight;
    private final int statusBarHeight;
    private View target;

    public InputMethodStateListener(View root) {
        this.target = root;
        screenHeight = target.getContext().getResources().getDisplayMetrics().heightPixels;
        statusBarHeight = DeviceInfoUtil.getStatusHeight(target.getContext());
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
    }

    @Override
    public void onGlobalLayout() {
        target.getWindowVisibleDisplayFrame(rect);
        //如果是全屏主题，bottom会比screenHeight大，因为screenHeight不算状态栏高度,而bottom是相对于屏幕左上角的绝对坐标
        int invisibleHeight = screenHeight + statusBarHeight - rect.bottom;//计算软键盘占有的高度  = 屏幕高度 - 视图可见高度
        Log.e("onGlobalLayout", "屏幕高度-> " + screenHeight + "   底部坐标-> " + rect.bottom + "  被遮挡高度-> " + invisibleHeight);
        if (invisibleHeight > keyHeight) {
            Log.e("onGlobalLayout", "监听到软键盘弹起");
            onInputMethodOpened(invisibleHeight);
        } else {
            Log.e("onGlobalLayout", "监听到软件盘关闭");
            onInputMethodClosed();
        }
    }

    protected abstract void onInputMethodClosed();

    protected abstract void onInputMethodOpened(int invisibleHeight);
}
