package com.style.view.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class RecordAudioView extends View {
    private int mWidth = 0;
    private int mHeight = 0;
    //振幅矩形宽度
    private int mHistogramWidth;
    //振幅矩形圆角大小
    private float mHistogramCorner;
    //振幅矩形最小高度
    private int mHistogramMinHeight;
    //振幅矩形最大高度
    private int mHistogramMaxHeight;
    //单边矩形个数
    private int mHistogramCount = 20;
    //振幅矩形间隔宽度
    private int mHistogramIntervalWidth;
    //中间空隙宽度
    private int mMiddleIntervalWidth;
    private Paint mHistogramPaint;
    private int COLOR_HISTOGRAM = 0xFFFF5E00;
    private float[] mData = new float[20];
    private RectF mRect = new RectF();

    public RecordAudioView(Context context) {
        this(context, null);
    }

    public RecordAudioView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordAudioView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHistogramWidth = dp2px(2.0f);
        mHistogramCorner = dp2px(1.0f);
        mHistogramMinHeight = dp2px(2.0f);
        mHistogramMaxHeight = dp2px(48.0f);
        mHistogramIntervalWidth = dp2px(3.0f);
        mMiddleIntervalWidth = dp2px(3.0f);
        for (int i = 0; i < mHistogramCount; i++)
            mData[i] = 0.0f;
        initPaint();
    }

    private void initPaint() {
        mHistogramPaint = new Paint();
        mHistogramPaint.setAntiAlias(true);
        mHistogramPaint.setColor(COLOR_HISTOGRAM);
        mHistogramPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = getPaddingLeft() + getPaddingRight() + mHistogramWidth * mHistogramCount * 2 + mHistogramIntervalWidth * (mHistogramCount - 1) * 2 + mMiddleIntervalWidth;
        mHeight = getPaddingBottom() + getPaddingTop() + mHistogramMaxHeight;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        for (int i = 0; i < mHistogramCount; i++) {
            float left = mMiddleIntervalWidth / 2 + (mHistogramWidth + mHistogramIntervalWidth) * i;
            float right = left + mHistogramWidth;
            float bottom = mHistogramMaxHeight / 2 * mData[i];
            bottom = (bottom < mHistogramMinHeight / 2) ? (mHistogramMinHeight / 2) : bottom;
            float top = -bottom;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //右端
                canvas.drawRoundRect(left, top, right, bottom, mHistogramCorner, mHistogramCorner, mHistogramPaint);
                //左端
                canvas.drawRoundRect(-right, top, -left, bottom, mHistogramCorner, mHistogramCorner, mHistogramPaint);
            } else {
                mRect.set(left, top, right, bottom);
                canvas.drawRoundRect(mRect, mHistogramCorner, mHistogramCorner, mHistogramPaint);
                mRect.set(-right, top, -left, bottom);
                canvas.drawRoundRect(mRect, mHistogramCorner, mHistogramCorner, mHistogramPaint);
            }
        }
        canvas.restore();
    }

    public void postValue(float v) {
        for (int i = 0; i < mHistogramCount; i++) {
            if (i < mHistogramCount - 1) {
                mData[i] = mData[i + 1];
                continue;
            }
            mData[i] = v;
        }
        postInvalidate();
    }

    public void reset() {
        for (int i = 0; i < mHistogramCount; i++)
            mData[i] = 0.0f;
        postInvalidate();
    }

    private int dp2px(float dipValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}