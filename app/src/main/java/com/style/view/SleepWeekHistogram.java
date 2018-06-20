package com.style.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.style.framework.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * 竖直柱状图
 */

public class SleepWeekHistogram extends View {
    private final String TAG = this.getClass().getSimpleName();
    private final String[] mWeeks;
    private int mViewHeight, mViewWidth;
    //虚线间隔宽度值
    private final int Y_AXIS_SCALE = dp2px(6.66f);
    //左右边缘距左右最近竖线距离
    private int mPadding;
    //两边竖线距离最近柱状图距离
    private int xOffset;
    //view顶部距离最近横线距离
    private int yTopOffset;
    //view底部距离最近横线距离
    private int yBottomOffset;
    //柱子与柱子间距离,柱子宽度
    private int mInterval, mHistogramWidth;
    //网格宽高
    private int mYAxisHeight, mXAxisWidth;
    private int mYTextHeight, mValueTextHeight;
    private int mTouchSlop, mTouchX, mTouchY;
    private int mYTextWidth, mXTextHeight;
    //默认9个小时
    private int mYMax = 9;
    private int mAverage, mAvailable;
    private Paint mHistogramPaint, mYPaint, mXPaint, mGridPaint, mAveragePaint;
    private List<PointItem> mValueList;
    private int percent = 100;
    private OnSelectionChangeListener mListener;
    private Path average;
    private PercentThread percentThread;

    public void setOnSelectionChangeListener(OnSelectionChangeListener listener) {
        mListener = listener;
    }

    private int mSelected = 0;

    public void setSelection(int selected) {
        mSelected = selected;
        invalidate();
    }

    public SleepWeekHistogram(Context context) {
        this(context, null);
    }

    public SleepWeekHistogram(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SleepWeekHistogram(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mWeeks = getContext().getResources().getStringArray(R.array.week_array);
        mValueList = new ArrayList<>();

        mViewWidth = getResources().getDisplayMetrics().widthPixels;
        mViewHeight = dp2px(300);
        mPadding = dp2px(20);
        xOffset = dp2px(20);
        yTopOffset = dp2px(30);
        yBottomOffset = dp2px(30);
        mInterval = dp2px(20);
        average = new Path();
        init();
    }

    private void init() {
        int mYTextSize = sp2px(12.0f);
        int mXTextSize = sp2px(12.0f);

        mHistogramPaint = new Paint();
        mHistogramPaint.setAntiAlias(true);
        mHistogramPaint.setTextSize(sp2px(10));
        mHistogramPaint.setTextAlign(Paint.Align.CENTER);
        mHistogramPaint.setColor(0xFF91c532);
        mHistogramPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        Paint.FontMetrics fm = mHistogramPaint.getFontMetrics();
        mValueTextHeight = (int) Math.abs(fm.descent - fm.ascent);

        mGridPaint = new Paint(mHistogramPaint);
        mGridPaint.setStyle(Paint.Style.FILL);

        mYPaint = new Paint(mHistogramPaint);
        mYPaint.setTextSize(mYTextSize);
        mYPaint.setColor(0x40FFFFFF);
        mYPaint.setTextAlign(Paint.Align.CENTER);

        mAveragePaint = new Paint();
        DashPathEffect pathEffect = new DashPathEffect(new float[]{Y_AXIS_SCALE, Y_AXIS_SCALE}, Y_AXIS_SCALE);
        mAveragePaint.setAntiAlias(true);
        mAveragePaint.setColor(0xffe6f1df);
        mAveragePaint.setStyle(Paint.Style.STROKE);
        mAveragePaint.setStrokeWidth(2);
        mAveragePaint.setPathEffect(pathEffect);

        mXPaint = new Paint();
        mXPaint.setAntiAlias(true);
        mXPaint.setTextSize(mXTextSize);
        mXPaint.setTextAlign(Paint.Align.CENTER);

        mYTextWidth = (int) mYPaint.measureText("4.5");
        Paint.FontMetrics fontMetrics = mYPaint.getFontMetrics();
        mYTextHeight = (int) Math.abs(fontMetrics.descent - fontMetrics.ascent);

        Paint.FontMetrics metrics = mXPaint.getFontMetrics();
        mXTextHeight = (int) Math.abs(metrics.descent - metrics.ascent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG, "onMeasure--" + mViewWidth + "  " + mViewHeight);
        setOtherParam();
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private void setOtherParam() {
        mXAxisWidth = mViewWidth - 2 * mPadding;
        mYAxisHeight = mViewHeight - yTopOffset - yBottomOffset;
        mHistogramWidth = (mXAxisWidth - xOffset * 2 - mInterval * 6) / 7;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGrid(canvas);
        drawHistogram(canvas);
    }

    private void drawGrid(Canvas canvas) {
        canvas.save();
        canvas.translate(mPadding, yTopOffset);
        mGridPaint.setColor(0x40FFFFFF);
        int gridWidth = mXAxisWidth / 4;
        int gridHeight = mYAxisHeight / 4;
        for (int i = 0; i <= 4; i++) {
            //画横线
            if (i == 0 || i == 2) {
                canvas.drawLine(0, i * gridHeight, mXAxisWidth - mYTextWidth / 2, i * gridHeight, mGridPaint);
            } else {
                canvas.drawLine(0, i * gridHeight, mXAxisWidth, i * gridHeight, mGridPaint);
            }
            //画竖线
            if (i != 4) {
                canvas.drawLine(i * gridWidth, 0, i * gridWidth, mYAxisHeight, mGridPaint);
            } else {
                canvas.drawLine(i * gridWidth, mYTextHeight / 2, i * gridWidth, mYAxisHeight / 2 - mYTextHeight / 2, mGridPaint);
                canvas.drawLine(i * gridWidth, mYAxisHeight / 2 + mYTextHeight / 2, i * gridWidth, mYAxisHeight, mGridPaint);
            }
        }
        //y轴刻度文字
        mYPaint.setColor(0x40FFFFFF);
        canvas.drawText(String.valueOf(mYMax), mXAxisWidth, 0 + (-mYPaint.getFontMetrics().ascent / 2), mYPaint);
        String mYMiddle = mYMax % 2 == 0 ? String.valueOf(mYMax / 2) : String.format(Locale.getDefault(), "%.1f", mYMax / 2.0f);
        canvas.drawText(mYMiddle, mXAxisWidth, mYAxisHeight / 2 + (-mYPaint.getFontMetrics().ascent / 2), mYPaint);
        canvas.restore();

        canvas.save();
        canvas.translate(0, yTopOffset + mYAxisHeight);
        mGridPaint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, mViewWidth, yBottomOffset, mGridPaint);
        canvas.restore();

        canvas.save();
        canvas.translate(mPadding + xOffset, yTopOffset + mYAxisHeight);
        mXPaint.setColor(0xFFAAAAAA);
        for (int i = 0; i < 7; i++) {
            canvas.drawText(mWeeks[i], i * mInterval + (mHistogramWidth + 1) * i + mHistogramWidth / 2, yBottomOffset / 2 + mXPaint.getFontMetrics().descent, mXPaint);
        }
        canvas.restore();

    }

    private void drawHistogram(Canvas canvas) {
        if (mValueList == null || mValueList.isEmpty() || mAvailable == 0) {
            showEmpty(canvas);
            return;
        }

        canvas.save();
        canvas.translate(mPadding + xOffset, yTopOffset + mYAxisHeight);
        Rect rect;
        int value;
        for (int i = 0; i < mValueList.size(); i++) {
            value = mValueList.get(i).yValue;
            if (value > 0) {
                rect = new Rect();
                rect.left = i * (mHistogramWidth + mInterval);
                rect.top = -(value * mYAxisHeight / (mYMax * 60)) * percent / 100;
                rect.right = rect.left + mHistogramWidth;
                rect.bottom = 0;
                if (mSelected == i) {
                    mHistogramPaint.setColor(Color.WHITE);
                    if (percent == 100) {
                        canvas.drawText(String.valueOf(value / 60) + "h" + value % 60 + "min",
                                rect.centerX(), rect.top - mXTextHeight / 4, mHistogramPaint);
                    }
                    if (mListener != null)
                        mListener.onSelectionChanged(mSelected);
                } else {
                    mHistogramPaint.setColor(0x99FFFFFF);
                }
                canvas.drawRect(rect, mHistogramPaint);
            }
        }
        if (percent == 100) {
            if (mAverage > 0 && mAvailable > 1) {
                int averageY = -mAverage * mYAxisHeight / (mYMax * 60);
                average.reset();
                average.moveTo(-xOffset, averageY);
                average.lineTo(mXAxisWidth - xOffset, averageY);
                canvas.drawPath(average, mAveragePaint);
            }
        }
        canvas.restore();
    }

    /**
     * 没有数据的时候展示空数据
     */
    private void showEmpty(Canvas canvas) {
        mYPaint.setColor(Color.WHITE);
        Rect rect = new Rect();
        rect.left = mPadding;
        rect.top = mYTextHeight / 2;
        rect.right = rect.left + mXAxisWidth;
        rect.bottom = rect.top + mYAxisHeight;
        canvas.drawText("无数据", rect.centerX(), getBaseLine(rect, mYPaint.getFontMetricsInt()), mYPaint);
    }

    public void setData(List<PointItem> list, boolean showAnimation) {
        mValueList.clear();
        if (list == null || list.isEmpty()) {
            invalidate();
            return;
        }
        mValueList.addAll(list);
        initOtherValue();
        if (showAnimation) {
            refreshWithAnimation();
            return;
        }
        percent = 100;
        invalidate();
    }

    private void initOtherValue() {
        mAverage = 0;
        int mMaxValue = 60 * mYMax;
        mAvailable = 0;
        int size = mValueList.size();
        int sleepTime;
        int totalTime = 0;
        for (int i = 0; i < size; i++) {
            PointItem data = mValueList.get(i);
            sleepTime = data.yValue;
            if (sleepTime > mMaxValue)
                mMaxValue = sleepTime;
            if (data.yValue > 0) {
                mAvailable++;
                totalTime += data.yValue;
            }
        }
        int maxK = mMaxValue % 60 == 0 ? mMaxValue / 60 : mMaxValue / 60 + 1;
        mYMax = maxK > mYMax ? maxK : mYMax;
        if (mAvailable > 0) {
            mAverage = totalTime / mAvailable;
        }
    }

    public void refreshWithAnimation() {
        percent = 0;
        if (percentThread != null && percentThread.isAlive()) {
            //停止上一次绘制线程，否则之前可能还在绘制
            percentThread.setStop();
            percentThread.interrupt();
        }
        percentThread = new PercentThread();
        percentThread.start();
    }

    private class PercentThread extends Thread {
        private boolean canContinue = true;

        public PercentThread() {
        }

        @Override
        public void run() {
            int sleepTime = 1;
            for (int i = 0; i <= 100; i += 10) {
                if (canContinue) {
                    if (i % 20 == 0) {
                        sleepTime += 5;
                    }
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    percent = i;
                    postInvalidate();
                }
            }
        }

        public void setStop() {
            canContinue = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int currentX = (int) event.getX();
        int currentY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取屏幕上点击的坐标
                mTouchX = (int) event.getX();
                mTouchY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                return false;
            case MotionEvent.ACTION_UP:
                if (Math.abs(currentX - mTouchX) <= mTouchSlop && Math.abs(currentY - mTouchY) <= mTouchSlop
                        && currentX > mPadding && currentX < mXAxisWidth + mPadding) {
                    return checkSelect(currentX, currentY);
                }
                break;
        }
        return false;
    }

    /**
     * 计算选中状态
     *
     * @param x
     * @param y
     */
    private boolean checkSelect(int x, int y) {
        if (mValueList == null || mValueList.isEmpty())
            return false;
        Rect rect;
        int selected = -1;
        for (int i = 0; i < mValueList.size(); i++) {
            rect = new Rect();
            rect.left = i * (mInterval + mHistogramWidth) + mPadding;
            rect.top = 0;
            rect.right = rect.left + mHistogramWidth;
            rect.bottom = mYAxisHeight / 2 + mYAxisHeight;
            if (mValueList.get(i).yValue > 0 && rect.contains(x, getScrollY() + y)) {
                selected = i;
            }
        }
        if (selected >= 0 && mSelected != selected) {
            mSelected = selected;
            invalidate();
            return true;
        }
        return false;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    private int dp2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
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

    public static class PointItem {
        public String xLabel;
        public int yValue;

        public PointItem(String xLabel, int yValue) {
            this.xLabel = xLabel;
            this.yValue = yValue;
        }
    }

    public interface OnSelectionChangeListener {
        /**
         * 选择状态改变
         *
         * @param selected 当前选中的Item 下标
         */
        void onSelectionChanged(int selected);
    }
}
