package com.style.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 血压历史曲线图
 */

public class BloodPressureChart extends View {
    private final String TAG = this.getClass().getSimpleName();

    /**
     * 图表的线的paint
     */
    private Paint mChartPaint = new Paint();
    /**
     * 填充的paint
     */
    private Paint mValuePaint = new Paint();
    /**
     * 横向和纵向分割线
     */
    private Paint mAxisPaint = new Paint();
    /**
     * 顶点
     */
    private Paint mPointPaint = new Paint();
    /**
     * 图表的坐标点的值的paint
     */
    private TextPaint mLabelXPaint = new TextPaint();
    private TextPaint mLabelYPaint = new TextPaint();
    private TextPaint mLabelValuePaint = new TextPaint();
    /**
     * 坐标文本高度
     */
    private float labelXHeight, labelYHeight;
    private float mOffset = 0, mVelocityX;
    private int mViewHeight, mViewWidth, mMaxOffset;
    private float mPadding, mYaxisHeight, mXaxisWidth, mYTextWidth, mXScale, mLastX;

    private List<Item> mItemList;
    private SimpleDateFormat mDayFormat = TimeUtil.getSimpleDateFormat("HH");
    private boolean mCanScroll;
    private Scroller mScroller;
    private int mItemWidth;
    private int mDataSize = 0, mMaxValue = 0;

    public BloodPressureChart(Context context) {
        this(context, null);
    }

    public BloodPressureChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BloodPressureChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        mXaxisWidth = mViewWidth - mPadding * 2 - mYTextWidth / 2;
        mYaxisHeight = mViewHeight - mPadding * 2 - labelXHeight;
        mXScale = mXaxisWidth / 10;
        mItemWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, getResources().getDisplayMetrics());
        init(context);
    }

    private void init(Context context) {
        mChartPaint.setAntiAlias(true);
        mChartPaint.setStyle(Paint.Style.FILL);
        mChartPaint.setStrokeJoin(Paint.Join.ROUND);// 笔刷图形样式
        mChartPaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔转弯的连接风格
        mChartPaint.setDither(true);//防抖动
        mChartPaint.setShader(null);
        mChartPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, getResources().getDisplayMetrics()));
        mChartPaint.setColor(0xFF45CE7B);

        mValuePaint.setAntiAlias(true);
        mValuePaint.setStyle(Paint.Style.FILL);
        mValuePaint.setTextAlign(Paint.Align.CENTER);
        mValuePaint.setStrokeJoin(Paint.Join.ROUND);// 笔刷图形样式
        mValuePaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔转弯的连接风格
        mValuePaint.setDither(true);//防抖动
        mValuePaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, getResources().getDisplayMetrics()));
        mValuePaint.setColor(0xff666666);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setStrokeJoin(Paint.Join.ROUND);// 笔刷图形样式
        mPointPaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔转弯的连接风格
        mPointPaint.setDither(true);//防抖动
        mPointPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, getResources().getDisplayMetrics()));
        mPointPaint.setColor(Color.BLACK);

        mAxisPaint.setAntiAlias(true);
        mAxisPaint.setStyle(Paint.Style.STROKE);
        mAxisPaint.setStrokeJoin(Paint.Join.ROUND);// 笔刷图形样式
        mAxisPaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔转弯的连接风格
        mAxisPaint.setDither(true);//防抖动
        mAxisPaint.setShader(null);
        mAxisPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics()));
        mAxisPaint.setColor(0x99CCCCCC);

        mLabelXPaint.setAntiAlias(true);
        mLabelXPaint.setColor(0xffaaaaaa);
        mLabelXPaint.setTextAlign(Paint.Align.CENTER);
        mLabelXPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        labelXHeight = mLabelXPaint.getFontMetrics().bottom - mLabelXPaint.getFontMetrics().top;

        mLabelYPaint.setAntiAlias(true);
        mLabelYPaint.setColor(0xffcccccc);
        mLabelYPaint.setTextAlign(Paint.Align.CENTER);
        mLabelYPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        labelYHeight = mLabelYPaint.getFontMetrics().bottom - mLabelYPaint.getFontMetrics().top;
        mYTextWidth = mLabelYPaint.measureText("200");

        mLabelValuePaint.setAntiAlias(true);
        mLabelValuePaint.setColor(0xff666666);
        mLabelValuePaint.setTextAlign(Paint.Align.CENTER);
        mLabelValuePaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG, "onMeasure--" + mViewWidth + "  " + mViewHeight);
        setMeasuredDimension(mViewWidth, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawGridsAndYLabel(canvas);
        drawPolyAndXLabel(canvas);
    }

    private void drawGridsAndYLabel(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.save();
        float xGridWidth = mXaxisWidth / 4;
        float yGridHeight = mYaxisHeight / 4;
        canvas.translate(mPadding, mPadding);
        for (int i = 0; i <= 4; i++) {
            if (i < 4) {
                canvas.drawLine(i * xGridWidth, 0, i * xGridWidth, mYaxisHeight, mAxisPaint);
            } else {
                canvas.drawLine(mXaxisWidth, labelYHeight / 2, mXaxisWidth, mYaxisHeight / 2 - labelYHeight / 2, mAxisPaint);
                canvas.drawLine(mXaxisWidth, mYaxisHeight / 2 + labelYHeight / 2, mXaxisWidth, mYaxisHeight, mAxisPaint);
            }
            if (i == 0 || i == 2) {
                canvas.drawLine(0, yGridHeight * i, mXaxisWidth - mYTextWidth / 2, yGridHeight * i, mAxisPaint);
            } else {
                canvas.drawLine(0, yGridHeight * i, mXaxisWidth, yGridHeight * i, mAxisPaint);
            }

            Rect rect = new Rect((int) (mXaxisWidth - mYTextWidth / 2), (int) (-mYaxisHeight / 2),
                    (int) (mXaxisWidth + mYTextWidth / 2), (int) (mYaxisHeight / 2));
            canvas.drawText("200", rect.centerX(), getBaseLine(rect, mLabelYPaint.getFontMetricsInt()), mLabelYPaint);
            rect = new Rect((int) (mXaxisWidth - mYTextWidth / 2), (int) (mYaxisHeight / 2 - mYaxisHeight / 2),
                    (int) (mXaxisWidth + mYTextWidth / 2), (int) (mYaxisHeight / 2 + mYaxisHeight / 2));
            canvas.drawText("100", rect.centerX(), getBaseLine(rect, mLabelYPaint.getFontMetricsInt()), mLabelYPaint);
        }
        canvas.restore();
    }

    private void drawPolyAndXLabel(Canvas canvas) {
        if (mItemList == null || mItemList.isEmpty() || mMaxValue == 0) {
            //drawEmpty(canvas);
            return;
        }
        canvas.save();
        canvas.translate(mPadding, mPadding + mYaxisHeight);
//        mFillPath.rewind();
//        mPolyPath.rewind();
        Item item;
        Rect rect;
        int nowX;
        //LinearGradient gradient;
        for (int i = 0; i < mDataSize; i++) {
            item = mItemList.get(i);
            nowX = (int) (item.x + mOffset);
            if (nowX >= 0 && nowX <= mXaxisWidth - mItemWidth / 2) {
                if (item.sbp > 0 && item.dbp > 0) {
                    //gradient = new LinearGradient(nowX, item.sY, nowX, item.dY, new int[]{0xff4fa213, 0xff91c532}, null, Shader.TileMode.MIRROR);
                    rect = new Rect(nowX - mItemWidth / 2, item.sY, nowX + mItemWidth / 2, item.dY);
                    //mChartPaint.setShader(gradient);
                    canvas.drawRect(rect, mChartPaint);
                    canvas.drawText(String.valueOf(item.sbp), nowX, getBaseLine((int) (item.sY - labelXHeight), item.sY,
                            mValuePaint.getFontMetricsInt()), mValuePaint);
                    canvas.drawText(String.valueOf(item.dbp), nowX, getBaseLine(item.dY, (int) (item.dY + labelXHeight),
                            mValuePaint.getFontMetricsInt()), mValuePaint);
                }
                canvas.drawText(item.hour, nowX, getBaseLine(0, (int) (labelXHeight + mPadding),
                        mLabelXPaint.getFontMetricsInt()), mLabelXPaint);
            }
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float rawX = event.getRawX();
        // 计算当前速度
        VelocityTracker velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(event);
        // 计算速度的单位时间
        velocityTracker.computeCurrentVelocity(1000);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = rawX;
                if (mScroller.computeScrollOffset())
                    mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDataSize > 7) {
                    // 计算偏移量
                    float offsetX = rawX - mLastX;
                    // 在当前偏移量的基础上增加偏移量
                    mOffset = mOffset + offsetX;
                    setOffsetRange();
                    // 偏移量修改后下次重绘会有变化
                    mLastX = rawX;
                    mVelocityX = velocityTracker.getXVelocity();
                    if (mCanScroll)
                        invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                int scrollX;
                if (mVelocityX > 0) {
                    scrollX = -mMaxOffset / 60;
                } else {
                    scrollX = mMaxOffset / 60;
                }
                if (Math.abs(mVelocityX) > 500) {
                    mScroller.startScroll(getScrollX(), 0, scrollX, 0, 2000);
                    invalidate();
                }
                mVelocityX = 0;

                Log.i("PPP", "mVelocityX = " + mVelocityX + "---------------");
                break;
        }
        // 计算完成后回收内存
        velocityTracker.clear();
        velocityTracker.recycle();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller == null) {
            mScroller = new Scroller(getContext(), new DecelerateInterpolator());
        }
        if (mScroller.computeScrollOffset()) {
            mOffset += mScroller.getCurrX();
            setOffsetRange();
            postInvalidate();
        }
    }

    /**
     * 对偏移量进行边界值判定
     */
    private void setOffsetRange() {
        if (mItemList != null && mItemList.size() > 7) {
            mMaxOffset = (int) (-mXScale * 1.5 * mDataSize + mXScale * 7 * 1.5);
            int offsetMin = 0;
            if (mOffset > offsetMin) {
                mCanScroll = false;
                mOffset = offsetMin;
            } else if (mOffset < mMaxOffset) {// 如果划出最大值范围
                mCanScroll = false;
                mOffset = mMaxOffset;
            } else {
                mCanScroll = true;
            }
        } else {
            mOffset = 0;
            mCanScroll = false;
        }
        if (mOffset > mXScale && mOffset % mXScale != 0) {
            mOffset = mMaxOffset - mOffset % mXScale;
        }
    }

    /**
     * 根据矩形区域换算文字的BaseLine
     *
     * @param rect
     * @return
     */
    private int getBaseLine(Rect rect, Paint.FontMetricsInt metricsInt) {
        return (rect.top + rect.bottom - metricsInt.bottom - metricsInt.top) / 2;
    }

    /**
     * 根据矩形区域换算文字的BaseLine
     *
     * @return
     */
    private int getBaseLine(int top, int bottom, Paint.FontMetricsInt metricsInt) {
        return (top + bottom - metricsInt.bottom - metricsInt.top) / 2;
    }

    private class Item {
        int sbp;
        int dbp;
        int x;
        int sY;
        int dY;
        String hour;
//        boolean select;
    }
}
