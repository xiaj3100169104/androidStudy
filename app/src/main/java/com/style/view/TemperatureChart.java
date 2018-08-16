package com.style.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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

import com.style.view.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 自定义部分滚动View
 */

public class TemperatureChart extends View {
    private final String TAG = this.getClass().getSimpleName();

    //图表线宽度
    private int lineWidth = 2;
    private Path mLinePath;
    ArrayList<TempItem> disList = new ArrayList<>();

    private Paint mChartPaint;
    private Paint mAxisPaint;
    private TextPaint mLabelXPaint;
    private TextPaint mLabelYPaint;

    //图表线颜色
    private int lineColor = 0xFF45CE7B;
    private int colorLabelY = 0xffaaaaaa;
    private int colorGrid = 0xFF45CE7B;
    private int colorChartText = 0xff666666;
    /**
     * 坐标文本高度
     */
    private float labelXHeight, labelYHeight;
    private int mViewHeight, mViewWidth;
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
    private float mItemWidth = 0;
    /**
     * 柱子间间隔宽度
     */
    private float mIntervalWidth;
    private ArrayList<TempItem> mItemList = new ArrayList<>();
    private static final float DEFAULT_MIN = 36.0F;
    private static final float DEFAULT_MAX = 38.0F;
    private static final float DEFAULT_MIN_SECOND = 34.0F;
    private static final float DEFAULT_MAX_SECOND = 40.0F;
    private float yMin = DEFAULT_MIN;
    private float yMax = DEFAULT_MAX;
    // 速度追踪
    private VelocityTracker velocityTracker = VelocityTracker.obtain();

    public TemperatureChart(Context context) {
        this(context, null);
    }

    public TemperatureChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TemperatureChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(getContext(), new DecelerateInterpolator());

        mPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        //mItemWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, getResources().getDisplayMetrics());
        mIntervalWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, getResources().getDisplayMetrics());
        lineWidth = Utils.dp2px(getContext(), lineWidth);
        mLinePath = new Path();
        initPaint(context);
        //setData(getData());
    }

    private void initPaint(Context context) {
        mAxisPaint = new Paint();
        mAxisPaint.setAntiAlias(true);
        mAxisPaint.setStyle(Paint.Style.STROKE);
        mAxisPaint.setDither(true);
        mAxisPaint.setShader(null);
        mAxisPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics()));
        mAxisPaint.setColor(colorGrid);

        mLabelYPaint = new TextPaint();
        mLabelYPaint.setAntiAlias(true);
        mLabelYPaint.setTextAlign(Paint.Align.CENTER);
        mLabelYPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        mLabelYPaint.setColor(colorLabelY);
        labelYHeight = mLabelYPaint.getFontMetrics().bottom - mLabelYPaint.getFontMetrics().top;
        mYTextWidth = mLabelYPaint.measureText("200");

        mLabelXPaint = new TextPaint(mLabelYPaint);
        mLabelXPaint.setColor(colorLabelY);
        labelXHeight = mLabelXPaint.getFontMetrics().bottom - mLabelXPaint.getFontMetrics().top;

        mChartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChartPaint.setStrokeJoin(Paint.Join.ROUND);// 笔刷图形样式
        mChartPaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔转弯的连接风格
        mChartPaint.setDither(true);//防抖动
        mChartPaint.setShader(null);//设置渐变色
        mChartPaint.setStyle(Paint.Style.STROKE);
        mChartPaint.setColor(lineColor);
        mChartPaint.setStrokeWidth(lineWidth);
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
        canvas.translate(mPadding + mYTextWidth, mPadding + mYaxisHeight);
        //画竖线
        canvas.drawLine(0, 0, 0, -mYaxisHeight, mAxisPaint);
        canvas.drawLine(mXaxisWidth, 0, mXaxisWidth, -mYaxisHeight, mAxisPaint);
        //画横线
        canvas.drawLine(0, 0, mXaxisWidth, 0, mAxisPaint);
        for (float i = yMin; i <= yMax; i += 1) {
            float y = -(mYaxisHeight * (i - yMin)) / (yMax - yMin);
            canvas.drawLine(0, y, -10, y, mAxisPaint);
            int yLabel = (int) i;
            canvas.drawText(String.valueOf(yLabel), -mYTextWidth, y + labelYHeight / 3, mLabelYPaint);
        }
        canvas.restore();
    }

    private void drawPolyAndXLabel(Canvas canvas) {
        if (mItemList == null || mItemList.isEmpty()) {
            //drawEmpty(canvas);
            return;
        }
        mLinePath.reset();
        canvas.save();
        canvas.translate(mPadding + mYTextWidth, mPadding + mYaxisHeight);

        TempItem item;
        //内部边距，防止柱子与y轴左右边界线重合
        float innerPadding = 0f;
        float originalX;
        float nowX;
        disList.clear();
        for (int i = 0; i < mItemList.size(); i++) {
            item = mItemList.get(i);
            originalX = (innerPadding + (mItemWidth + mIntervalWidth) * i);
            nowX = (originalX + mOffset);
            if (nowX >= innerPadding && nowX <= mXaxisWidth - innerPadding) {
                if (item.yValue >= yMin && item.yValue <= yMax) {
                    disList.add(item);
                   /* float y = -(mYaxisHeight * (item.yValue - yMin)) / (yMax - yMin);
                    float y2 = -(mYaxisHeight * (nextItem.yValue - yMin)) / (yMax - yMin);
                    canvas.drawLine(nowX, y, nextX, y2, mChartPaint);*/
                }
                if (i % 5 == 0)
                    canvas.drawText(item.xLabel, nowX, getBaseLine(0, (int) (labelXHeight + mPadding), mLabelXPaint.getFontMetricsInt()), mLabelXPaint);
            }
        }
        for (int i = 0; i < disList.size(); i++) {
            item = mItemList.get(i);
            originalX = (innerPadding + (mItemWidth + mIntervalWidth) * i);
            nowX = (originalX + mOffset);
            float y = -(mYaxisHeight * (item.yValue - yMin)) / (yMax - yMin);
            if (i == 0) {
                mLinePath.moveTo(nowX, y);
            } else if (i == disList.size() - 1) {
                mLinePath.lineTo(nowX, y);
                mLinePath.lineTo(nowX, 0);
                mLinePath.lineTo(nowX, y);
                mLinePath.lineTo(nowX, y);
            } else {
                mLinePath.lineTo(nowX, y);
            }
        }
        canvas.restore();
    }

    /**
     * 根据矩形区域换算文字的BaseLine
     * 绘制在top与bottom中间的文字
     *
     * @return
     */
    private int getBaseLine(int top, int bottom, Paint.FontMetricsInt metricsInt) {
        return (top + bottom - metricsInt.bottom - metricsInt.top) / 2;
    }

    public void setData(List<TempItem> list) {
        mItemList.clear();
        yMin = DEFAULT_MIN;
        yMax = DEFAULT_MAX;
        if (list != null && !list.isEmpty()) {
            for (TempItem item : list) {
                if (item.yValue > DEFAULT_MAX || item.yValue < DEFAULT_MIN) {
                    yMax = DEFAULT_MAX_SECOND;
                    yMin = DEFAULT_MIN_SECOND;
                }
            }
            mItemList.addAll(list);
            mMinOffset = -((mItemWidth + mIntervalWidth) * mItemList.size() - mXaxisWidth);
        }
        invalidate();
    }

    private List<TempItem> getData() {
        ArrayList<TempItem> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            TempItem b = new TempItem(random.nextFloat() + 36.5f, String.valueOf(i));
            list.add(b);
        }
        return list;
    }

    /**
     * 偏移量最大值，最小值，当前偏移量,由于向左滑，所以最大值为0，如果最小值大于或等于0表示不需要移动
     */
    private float mMaxOffset = 0, mMinOffset = 0, mOffset = 0;
    private float mLastX;
    private boolean mCanRefresh;
    private Scroller mScroller;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getRawX();
                if (mScroller.computeScrollOffset())
                    mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMinOffset < 0) {
                    float rawXMove = event.getRawX();
                    // 计算偏移量
                    float offsetX = rawXMove - mLastX;
                    // 在当前偏移量的基础上增加偏移量
                    mOffset = mOffset + offsetX;
                    setCanRefresh();
                    // 偏移量修改后下次重绘会有变化
                    mLastX = rawXMove;
                    if (mCanRefresh)
                        invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                // 计算速度的单位时间,最大速度为5px/ms
                velocityTracker.computeCurrentVelocity(1, 5);
                float mVelocityX = velocityTracker.getXVelocity();
                // 计算完成后回收内存
                velocityTracker.clear();
                //velocityTracker.recycle();
                Log.e(TAG, "mVelocityX = " + mVelocityX + " px/ms");
                //偏移量已经是边界时不用再计算滚动逻辑
                if (mOffset > mMinOffset && mOffset < mMaxOffset && mVelocityX != 0) {
                    float dx;//速度越大滚动距离理应越大,假设速度为5px/ms时，最大滑动位移5000px，设置花费时间为3000ms。以此为标准.速度越大，位移越大，时间越长。
                    int duration;
                    dx = mVelocityX / (5f / 5000f);
                    duration = (int) Math.abs(mVelocityX / (5f / 3000f));
                    Log.e(TAG, "dx = " + dx + " px" + "  duration = " + duration + " ms");
                    /*if (mVelocityX > 0) {//向右滑
                        duration = (dx / mVelocityX);
                    } else {//左滑，内容左移，偏移量应该减小，这里设置负数
                        duration = (dx / mVelocityX);
                    }*/
                    if (dx != 0) {
                        //scroller.getCurrX() = mStartX + Math.round(x * dx);  x等于从0逐渐增大到1.
                        mScroller.startScroll((int) mOffset, 0, (int) dx, 0, duration < 500 ? 500 : duration);//duration太小会有跳动效果，不平滑
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            if (x >= mMinOffset && x <= mMaxOffset) {
                mOffset = x;
                setCanRefresh();
                if (mCanRefresh)
                    postInvalidate();
            }
        } else {
            mScroller.forceFinished(true);
        }
    }

    /**
     * 对偏移量进行边界值判定
     */
    private void setCanRefresh() {
        mOffset = mOffset >= mMaxOffset ? mMaxOffset : mOffset;
        mOffset = mOffset <= mMinOffset ? mMinOffset : mOffset;
        //Log.i(TAG, "mOffset = " + mOffset);
        if (mOffset >= mMinOffset && mOffset <= mMaxOffset) {
            mCanRefresh = true;
        } else {
            mCanRefresh = false;
        }
    }

    public static class TempItem {
        public float yValue;
        public String xLabel;

        public TempItem(float yValue, String xLabel) {
            this.yValue = yValue;
            this.xLabel = xLabel;
        }
    }
}
