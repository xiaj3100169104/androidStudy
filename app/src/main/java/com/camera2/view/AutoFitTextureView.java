package com.camera2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

/**
 * Created by wangyt on 2019/4/25
 */
public class AutoFitTextureView extends TextureView {

    private int ratioW = 0;
    private int ratioH = 0;

    public AutoFitTextureView(Context context) {
        super(context);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.e("onMeasure", width + "x" + height);
        if ((0 == ratioW || 0 == ratioH)) {
            //未设定宽高比，使用预览窗口默认宽高
            setMeasuredDimension(width, height);
        } else {
            float scale = (ratioH + 0.0f) / ratioW;
            if ((height + 0.0f) / width == ratioW) {
                setMeasuredDimension(width, height);
                return;
            }
            //设定宽高比，调整预览窗口大小（调整后窗口大小不超过默认值）
            if ((height + 0.0f) / width > scale) {
                height = (int) (width * scale);
                setMeasuredDimension(width, height);
            } else {
                width = (int) (height / scale);
                setMeasuredDimension(width, height);
            }
        }
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
