package com.camera2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by wangyt on 2019/4/25
 */
public class TestView extends View {

    private int ratioW = 0;
    private int ratioH = 0;

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置宽高比
     *
     * @param width
     * @param height
     */
    public void setAspectRation(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("width or height can not be negative.");
        }
        ratioW = width;
        ratioH = height;
        //请求重新布局
        requestLayout();
        //invalidate();
    }

    /**
     * 根布局是RelativeLayout时，改变宽度无效，暂时不知道原因
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.e("onMeasure", width + "x" + height);
        if ((0 != ratioW && 0 != ratioH)) {
            float scale = (ratioH + 0.0f) / ratioW;
            //高宽比不同时
            if ((height + 0.0f) / width != ratioW) {
                //设定宽高比，调整预览窗口大小（调整后窗口大小不超过默认值）
                if ((height + 0.0f) / width > scale) {
                    height = (int) (width * scale);
                } else {
                    width = (int) ((height + 0.0f) / scale);
                }
            }
        }
        setMeasuredDimension(width, height);
        Log.v("onMeasure", width + "x" + height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("onSizeChanged", w + "x" + h + "  " + oldw + "x" + oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e("onLayout", left + " " + top + "  " + right + " " + bottom);
    }


}
