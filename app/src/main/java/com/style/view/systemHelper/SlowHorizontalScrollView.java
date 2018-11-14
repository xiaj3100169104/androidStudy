package com.style.view.systemHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by xiajun on 2018/5/7.
 */

public class SlowHorizontalScrollView extends HorizontalScrollView {
    public SlowHorizontalScrollView(Context context) {
        super(context);
    }
    public SlowHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SlowHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 1);//这里设置滑动的速度
    }
}
