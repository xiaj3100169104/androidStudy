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
import java.util.Locale;
import java.util.Random;

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
    private float mVelocityX;
    private int mViewHeight, mViewWidth;
    /**
     * 偏移量最大值，最小值，当前偏移量,由于向左滑，所以最大值为0，如果最小值大于或等于0表示不需要移动
     */
    private float mMaxOffset = 0, mMinOffset = 0, mOffset = 0;
    /**
     * 边距
     */
    private float mPadding;
    /**
     * 网格宽高
     */
    private float mYaxisHeight, mXaxisWidth;
    /**
     * 纵坐标文本高度
     */
    private float mYTextWidth;
    /**
     * 柱子宽度
     */
    private float mItemWidth;
    /**
     * 柱子间间隔宽度
     */
    private float mXScale;
    private float mLastX;

    private ArrayList<Item> mItemList = new ArrayList<>();
    private SimpleDateFormat mDayFormat = new SimpleDateFormat("HH", Locale.getDefault());
    private boolean mCanScroll;
    private Scroller mScroller;
    private float yMin = 0f;
    private float yMax = 200f;

    public BloodPressureChart(Context context) {
        this(context, null);
    }

    public BloodPressureChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BloodPressureChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(getContext(), new DecelerateInterpolator());

        mPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        mItemWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, getResources().getDisplayMetrics());
        mXScale = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, getResources().getDisplayMetrics());
        init(context);
        setData(getData());
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
        mXaxisWidth = mViewWidth - mPadding * 2 - mYTextWidth;
        mYaxisHeight = mViewHeight - mPadding * 2 - labelXHeight;
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
        canvas.translate(mPadding + mYTextWidth, mPadding);
        for (int i = 0; i <= 4; i++) {
            //画横线
            canvas.drawLine(0, yGridHeight * i, mXaxisWidth, yGridHeight * i, mAxisPaint);
            //画竖线
            canvas.drawLine(xGridWidth * i, 0, xGridWidth * i, mYaxisHeight, mAxisPaint);
            int yLabel = 200 - 50 * i;
            canvas.drawText(String.valueOf(yLabel), -mYTextWidth, yGridHeight * i + labelYHeight / 2, mLabelYPaint);

        }
        canvas.restore();
    }

    private void drawPolyAndXLabel(Canvas canvas) {
        if (mItemList == null || mItemList.isEmpty()) {
            //drawEmpty(canvas);
            return;
        }
        canvas.save();
        canvas.translate(mPadding + mYTextWidth, mPadding + mYaxisHeight);

        Item item;
        //内部边距，防止柱子超出y轴网格线
        float innerPadding = 50f;
        float originalX;
        float nowX;
        //LinearGradient gradient;
        for (int i = 0; i < mItemList.size(); i++) {
            item = mItemList.get(i);
            originalX = (int) (innerPadding + (mItemWidth + mXScale) * i);
            nowX = (int) (originalX + mOffset);
            if (nowX >= 0 && nowX <= mXaxisWidth - innerPadding) {
                if (item.yLow > 0 && item.yHigh < 200) {
                    //gradient = new LinearGradient(nowX, item.sY, nowX, item.dY, new int[]{0xff4fa213, 0xff91c532}, null, Shader.TileMode.MIRROR);
                    //mChartPaint.setShader(gradient);
                    float top = -(mYaxisHeight * (item.yHigh - yMin)) / (yMax - yMin);
                    float bottom = -(mYaxisHeight * (item.yLow - yMin)) / (yMax - yMin);
                    canvas.drawRect(nowX - mItemWidth / 2, top, nowX + mItemWidth / 2, bottom, mChartPaint);
                    //canvas.drawText(String.valueOf(item.sbp), nowX, getBaseLine((int) (item.sY - labelXHeight), item.sY, mValuePaint.getFontMetricsInt()), mValuePaint);
                    //canvas.drawText(String.valueOf(item.dbp), nowX, getBaseLine(item.dY, (int) (item.dY + labelXHeight), mValuePaint.getFontMetricsInt()), mValuePaint);
                }
                canvas.drawText(item.xLabel, nowX, getBaseLine(0, (int) (labelXHeight + mPadding), mLabelXPaint.getFontMetricsInt()), mLabelXPaint);
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
                if (mMinOffset < 0) {
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
                Log.i(TAG, "mVelocityX = " + mVelocityX + " px/s");
                int dx;//速度越大滚动距离理应越大
                if (mVelocityX > 0) {
                    dx = 100;
                } else {
                    dx = -100;
                }
                if (Math.abs(mVelocityX) > 500) {
                    //scroller.getCurrX() = mStartX + Math.round(x * dx);  x等于从0逐渐增大到1.
                    mScroller.startScroll((int) mOffset, 0, dx, 0, 2000);
                    invalidate();
                }
                mVelocityX = 0;
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

        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            if (x >= mMinOffset && x <= mMaxOffset) {
                mOffset = x;
                setOffsetRange();
                postInvalidate();
            }
        }
    }

    public void setData(List<Item> list) {
        mItemList.clear();
        if (list != null && !list.isEmpty()) {
            mItemList.addAll(list);
            mMinOffset = -((mItemWidth + mXScale) * mItemList.size() - mXaxisWidth);
        }
        //invalidate();
    }

    private List<Item> getData() {
        ArrayList<Item> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            Item b = new Item(random.nextInt(50) + 20, random.nextInt(90) + 30, String.valueOf(i));
            list.add(b);
        }
        return list;
    }

    /**
     * 对偏移量进行边界值判定
     */
    private void setOffsetRange() {
        if (mOffset >= mMinOffset && mOffset <= mMaxOffset) {
            mCanScroll = true;
        } else {
            mCanScroll = false;
        }
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
        public int yLow;
        public int yHigh;
        public String xLabel;

        public Item(int yLow, int yHigh, String xLabel) {
            this.yLow = yLow;
            this.yHigh = yHigh;
            this.xLabel = xLabel;
        }
    }
}
