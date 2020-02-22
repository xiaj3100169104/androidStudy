package com.style.view.healthy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.style.lib_custom_view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 竖直柱状图
 */

public class SportWeekHistogram extends View {
    //网格线颜色
    private static final int COLOR_GRID = 0x99CCCCCC;
    private static final int COLOR_Y_LABEL = 0xFFAAAAAA;
    private static final int COLOR_X_LABEL = 0xFFAAAAAA;
    private static final int COLOR_HISTOGRAM = 0xFF45CE7B;
    private static final int COLOR_HISTOGRAM_SELECTED = 0xffB8FAD6;
    private static final int COLOR_HISTOGRAM_VALUE = 0xFF45CE7B;
    private final String TAG = this.getClass().getSimpleName();
    private final String[] mWeeks;
    private int mViewHeight, mViewWidth;
    //虚线间隔宽度值
    private final int Y_AXIS_SCALE = dp2px(6.66f);
    //X轴起始偏移宽度
    private float xStartOffset = 40;
    //X轴结束偏移宽度
    private float xEndOffset = 20;
    //两边竖线距离最近x刻度距离
    private float xOffset = 20;
    //view顶部距离最近横线距离
    private float yTopOffset = 30;
    //view底部距离最近横线距离
    private float yBottomOffset = 30;
    //柱子宽度
    private float mHistogramWidth = 20;
    //网格宽高
    private float mYAxisHeight, mXAxisWidth;

    //默认以5000为间隔
    private static final int DEFAULT_Y_INTERVAL = 5000;
    //默认5k
    private int mYMax = DEFAULT_Y_INTERVAL;
    private int mYMin = 0;

    private Paint mHistogramPaint, mGridPaint;
    private TextPaint mYPaint;
    private List<PointItem> mValueList;
    private int percent = 100;
    private OnSelectionChangeListener mListener;
    private Path average;
    private PercentThread percentThread;
    private int mYTextSize;
    private int mXTextSize;
    private int mHistogramValueTextSize;

    public void setOnSelectionChangeListener(OnSelectionChangeListener listener) {
        mListener = listener;
    }

    private int mSelected = -1;

    public void setSelection(int selected) {
        mSelected = selected;
        invalidate();
    }

    public SportWeekHistogram(Context context) {
        this(context, null);
    }

    public SportWeekHistogram(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SportWeekHistogram(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mWeeks = getContext().getResources().getStringArray(R.array.week_array);
        mValueList = new ArrayList<>();

        xStartOffset = dp2px(xStartOffset);
        xEndOffset = dp2px(xEndOffset);
        xOffset = dp2px(xOffset);
        yTopOffset = dp2px(yTopOffset);
        yBottomOffset = dp2px(yBottomOffset);
        mHistogramWidth = dp2px(mHistogramWidth);
        mYTextSize = sp2px(12.0f);
        mXTextSize = sp2px(12.0f);
        mHistogramValueTextSize = sp2px(10.0f);
        average = new Path();
        init();
    }

    private void init() {
        mGridPaint = new Paint();
        mGridPaint.setAntiAlias(true);
        mGridPaint.setColor(COLOR_GRID);

        mYPaint = new TextPaint(mGridPaint);
        mYPaint.setTextSize(mYTextSize);
        mYPaint.setTextAlign(Paint.Align.CENTER);

        mHistogramPaint = new Paint(mGridPaint);
        mHistogramPaint.setColor(COLOR_HISTOGRAM);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG, "onMeasure--" + mViewWidth + "  " + mViewHeight);
        setMeasuredDimension(mViewWidth, mViewHeight);

        mXAxisWidth = mViewWidth - xStartOffset - xEndOffset;
        mYAxisHeight = mViewHeight - yTopOffset - yBottomOffset;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(xStartOffset, yTopOffset + mYAxisHeight);
        mGridPaint.setColor(COLOR_GRID);
        //y轴刻度文字
        mYPaint.setColor(COLOR_Y_LABEL);
        mYPaint.setTextSize(mYTextSize);
        float mYTextWidth = mYPaint.measureText("10k");
        float gridWidth = mXAxisWidth / 4;
        float gridHeight = mYAxisHeight / 5;
        //最大值等于5000步时
        if (mYMax == DEFAULT_Y_INTERVAL) {
            for (int i = 0; i <= mYMax; i += 1000) {
                //画横线
                float y = -mYAxisHeight * (i - mYMin) / (mYMax - mYMin);
                canvas.drawLine(0, y, mXAxisWidth, y, mGridPaint);
                if (i / 1000 % 2 == 1) {
                    canvas.drawText(i / 1000 + "k", -mYTextWidth, y + getBaseLine2CenterY(mYPaint.getFontMetrics()), mYPaint);
                }
            }
        } else if (mYMax > DEFAULT_Y_INTERVAL) {
            for (int i = mYMin; i <= mYMax; i += 2500) {
                //画横线
                float y = -mYAxisHeight * (i - mYMin) / (mYMax - mYMin);
                canvas.drawLine(0, y, mXAxisWidth, y, mGridPaint);
                if (i % 5000 == 0 && i > 0) {
                    canvas.drawText(i / 1000 + "k", -mYTextWidth, y + getBaseLine2CenterY(mYPaint.getFontMetrics()), mYPaint);
                }
            }
        }
        for (int i = 0; i <= 4; i++) {
            //画竖线
            float x = gridWidth * i;
            canvas.drawLine(x, 0, x, -mYAxisHeight, mGridPaint);
        }
        //String mYMiddle = mYMax % 2 == 0 ? String.valueOf(mYMax / 2) : String.format(Locale.getDefault(), "%.1f", mYMax / 2.0f);
        //canvas.drawText(mYMiddle, mXAxisWidth, mYAxisHeight / 2 + (-mYPaint.getFontMetrics().ascent / 2), mYPaint);
        canvas.restore();
        //画x标签白色背景
        canvas.save();
        canvas.translate(0, yTopOffset + mYAxisHeight);
        mGridPaint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, mViewWidth, yBottomOffset, mGridPaint);
        canvas.restore();
        //画x标签
        canvas.save();
        canvas.translate(xStartOffset + xOffset, yTopOffset + mYAxisHeight);
        mYPaint.setColor(COLOR_X_LABEL);
        mYPaint.setTextSize(mXTextSize);
        float scaleWidth = (mXAxisWidth - xOffset * 2) / 6;
        for (int i = 0; i < 7; i++) {
            canvas.drawText(mWeeks[i], scaleWidth * i, Math.abs(mYPaint.getFontMetrics().top) + dp2px(5), mYPaint);
        }
        if (mValueList == null || mValueList.isEmpty()) {
            canvas.restore();
            //mYPaint.setColor(Color.WHITE);
            //canvas.drawText(getContext().getString(R.string.week_no_data), mViewWidth / 2, mViewHeight / 2, mYPaint);
            return;
        }
        //画柱状图
        Rect rect;
        int value;
        for (int i = 0; i < mValueList.size(); i++) {
            value = mValueList.get(i).yValue;
            if (value > 0) {
                rect = new Rect();
                rect.left = (int) (scaleWidth * i - mHistogramWidth / 2);
                rect.top = (int) (-mYAxisHeight * (value - mYMin) / (mYMax - mYMin) * percent / 100f);
                rect.right = (int) (rect.left + mHistogramWidth);
                rect.bottom = 0;
                if (mSelected == i) {
                    mHistogramPaint.setColor(COLOR_HISTOGRAM_SELECTED);
                    mYPaint.setColor(COLOR_HISTOGRAM_VALUE);
                    mYPaint.setTextSize(mHistogramValueTextSize);
                    if (percent == 100) {
                        canvas.drawText(String.valueOf(value), rect.centerX(), rect.top - mYPaint.getFontMetrics().bottom, mYPaint);
                    }
                } else {
                    mHistogramPaint.setColor(COLOR_HISTOGRAM);
                }
                canvas.drawRect(rect, mHistogramPaint);
            }
        }
        canvas.restore();
    }

    public void setData(List<PointItem> list, boolean showAnimation, int selected) {
        mValueList.clear();
        mSelected = -1;
        if (list == null || list.isEmpty()) {
            mYMax = DEFAULT_Y_INTERVAL;
            invalidate();
            return;
        }
        mSelected = selected;
        int max = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).yValue > max) {
                max = list.get(i).yValue;
            }
        }
        //动态计y最大值，5000的倍数
        mYMax = DEFAULT_Y_INTERVAL * (max / DEFAULT_Y_INTERVAL + 1);
        mValueList.addAll(list);
        if (showAnimation) {
            refreshWithAnimation();
            return;
        }
        percent = 100;
        invalidate();
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

    /**
     * 计算绘制文字时的基线到中轴线的距离
     *
     * @param fontMetrics
     * @return 基线和centerY的距离
     */
    public static float getBaseLine2CenterY(Paint.FontMetrics fontMetrics) {
        return (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
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

    private float mTouchSlop, mTouchDownX, mTouchDownY, mTouchMoveX, mTouchMoveY;
    //是否处于手指拖动中
    private boolean mIsBeingDragged = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Log.e(TAG, "onTouchEvent   " + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = event.getX();
                mTouchDownY = event.getY();
            case MotionEvent.ACTION_MOVE:
                mTouchMoveX = event.getX();
                mTouchMoveY = event.getY();
                int distanceX = (int) (mTouchMoveX - mTouchDownX);
                if (!mIsBeingDragged && Math.abs(distanceX) > mTouchSlop) {
                    mIsBeingDragged = true;
                }
                if (mIsBeingDragged)
                    if (getParent() != null) getParent().requestDisallowInterceptTouchEvent(true);

                break;
            //MOVE事件未拦截，也不会走UP事件
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                float mTouchUpX = event.getX();
                //mTouchUpY = event.getY();
                int translationX = (int) (mTouchUpX - mTouchDownX);
                //是否拖动过
                if (mIsBeingDragged) {
                    //向右
                    if (translationX > mTouchSlop) {
                        if (mListener != null)
                            mListener.onSlideRight();
                    }
                    if (translationX < -mTouchSlop) {
                        if (mListener != null)
                            mListener.onSlideLeft();

                    }
                } else {
                    if (mListener != null)
                        checkSelect((int) mTouchDownX, (int) mTouchDownY);
                }
                mIsBeingDragged = false;
                break;
        }
        return true;
    }

    /**
     * 计算选中状态，只判断左右边界
     *
     * @param x
     * @param y
     */
    private boolean checkSelect(int x, int y) {
        if (mValueList == null || mValueList.isEmpty())
            return false;
        Rect rect;
        int selected = -1;
        float scaleWidth = (mXAxisWidth - xOffset * 2) / 6;
        for (int i = 0; i < mValueList.size(); i++) {
            rect = new Rect();
            rect.left = (int) (xStartOffset + xOffset + (scaleWidth * i - mHistogramWidth / 2));
            rect.top = (int) yTopOffset;
            rect.right = (int) (rect.left + mHistogramWidth);
            rect.bottom = (int) (yTopOffset + mYAxisHeight);
            if (mValueList.get(i).yValue > 0 && x >= rect.left && x <= rect.right) {
                selected = i;
            }
        }
        if (selected >= 0 && mSelected != selected) {
            mSelected = selected;
            invalidate();
            mListener.onSelectionChanged(mSelected);
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

        public void onSlideLeft();

        public void onSlideRight();
    }
}
