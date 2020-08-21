package com.style.view.healthy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import androidx.annotation.Nullable;

import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 自定义部分滚动View
 */

public class HeartLineChart extends View {
    private final String TAG = this.getClass().getSimpleName();

    //图表线宽度
    private int lineWidth = 2;
    private Path mLinePath;
    private Paint mChartPaint;
    private TextPaint mLabelXPaint;
    private TextPaint mLabelYPaint;
    //图表线颜色
    private int lineColor = 0xFF45CE7B;
    private int colorLabelY = 0xffaaaaaa;
    private int colorGrid = 0x99CCCCCC;
    //坐标文本高度
    private float labelXHeight, labelYHeight;
    private int mViewHeight, mViewWidth;
    //边距
    private float mPaddingTop = 15, mPaddingBottom = 30, mPaddingLeft = 40, mPaddingRight = 15;
    //网格宽高
    private float mYaxisHeight, mXaxisWidth;
    //纵坐标文本高度
    private float mYTextWidth;
    //横坐标间隔
    private float mIntervalWidth;

    private static final float DEFAULT_MIN = 40F;
    private static final float DEFAULT_MAX = 120F;
    private static final float DEFAULT_MIN_SECOND = 0F;
    private static final float DEFAULT_MAX_SECOND = 200F;
    private float yMin = DEFAULT_MIN;
    private float yMax = DEFAULT_MAX;

    private ArrayList<HeartLineItem> mItemList = new ArrayList<>();
    private ArrayList<Integer> mIndexList = new ArrayList<>();
    //左绘制边界
    private float leftDrawOffset;
    //右绘制边界
    private float rightDrawOffset;

    public HeartLineChart(Context context) {
        this(context, null);
    }

    public HeartLineChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(getContext(), new DecelerateInterpolator());

        mPaddingTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPaddingTop, getResources().getDisplayMetrics());
        mPaddingBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPaddingBottom, getResources().getDisplayMetrics());
        mPaddingLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPaddingLeft, getResources().getDisplayMetrics());
        mPaddingRight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPaddingRight, getResources().getDisplayMetrics());
        lineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, lineWidth, getResources().getDisplayMetrics());
        mIntervalWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, getResources().getDisplayMetrics());
        mMinOffset = -2 * mIntervalWidth;
        mLinePath = new Path();
        initPaint(context);
    }

    private void initPaint(Context context) {
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
        mXaxisWidth = mViewWidth;
        mYaxisHeight = mViewHeight - mPaddingTop - mPaddingBottom;
        leftDrawOffset = -mIntervalWidth * 2;
        rightDrawOffset = mViewWidth + mIntervalWidth * 2;
        Log.e(TAG, "onMeasure--" + mViewWidth + "  " + mViewHeight);
        setMeasuredDimension(mViewWidth, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPolyAndXLabel(canvas);
    }

    private void drawPolyAndXLabel(Canvas canvas) {
        if (mItemList.size() <= 1) {
            return;
        }
        mIndexList.clear();
        for (int i = 0; i < mItemList.size(); i++) {
            float x = (mIntervalWidth + mIntervalWidth * i) - mOffset;
            if (x > mMinOffset && x < mMaxOffset)
                mIndexList.add(i);
            if (x > mMaxOffset)
                break;
        }
        canvas.save();
        canvas.translate(0, mPaddingTop + mYaxisHeight);
        mLinePath.reset();
        float x, y;
        for (int i = 0; i < mIndexList.size(); i++) {
            x = (mIntervalWidth + mIntervalWidth * mIndexList.get(i)) - mOffset;
            int yValue = mItemList.get(mIndexList.get(i)).yValue;
            y = -(mYaxisHeight * (yValue - yMin)) / (yMax - yMin);
            if (i == 0) {
                mLinePath.moveTo(x, y);
            } else {
                mLinePath.lineTo(x, y);
            }
            if (mIndexList.get(i) % 5 == 0)
                canvas.drawText(mItemList.get(mIndexList.get(i)).xLabel, x, mPaddingBottom / 2 + getBaseLine2CenterY(mLabelXPaint.getFontMetrics()), mLabelXPaint);

        }
        canvas.drawPath(mLinePath, mChartPaint);
        canvas.restore();
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

    public void setData(List<HeartLineItem> list) {
        mScroller.abortAnimation();
        mItemList.clear();
        yMin = DEFAULT_MIN;
        yMax = DEFAULT_MAX;
        if (list != null && !list.isEmpty()) {
            for (HeartLineItem item : list) {
                if (item.yValue > DEFAULT_MAX || item.yValue < DEFAULT_MIN) {
                    yMax = DEFAULT_MAX_SECOND;
                    yMin = DEFAULT_MIN_SECOND;
                }
            }
            mItemList.addAll(list);
        }
        mMaxOffset = (int) (mIntervalWidth * (mItemList.size() - 1) - mIntervalWidth * (mViewWidth / mIntervalWidth - 2));
        mMaxOffset = Math.max(mMaxOffset, 0);
        mOffset = 0;
        Log.e(TAG, "mMaxOffset = " + mMaxOffset);
        requestLayout();
        invalidate();
    }

    public void scrollToRight() {
        post(() -> {
            mOffset = mMinOffset;
            invalidate();
        });
    }

    private List<HeartLineItem> getData() {
        ArrayList<HeartLineItem> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            HeartLineItem b = new HeartLineItem(random.nextInt(40) + 60, String.valueOf(i));
            list.add(b);
        }
        return list;
    }

    private float mMaxOffset = 0, mMinOffset = 0, mOffset = 0;
    private float mLastX;
    private boolean mCanRefresh;
    private Scroller mScroller;
    // 速度追踪
    private VelocityTracker velocityTracker = VelocityTracker.obtain();
    private float mTouchSlop, mTouchDownX, mTouchDownY, mTouchMoveX, mTouchMoveY;
    //是否处于手指拖动中
    private boolean mIsBeingDragged = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Log.e(TAG, "onTouchEvent   " + action);
        velocityTracker.addMovement(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = event.getX();
                mTouchDownY = event.getY();
                mLastX = event.getX();
                if (mScroller.computeScrollOffset())
                    mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                mTouchMoveX = event.getX();
                mTouchMoveY = event.getY();
                int distanceX = (int) (mTouchMoveX - mTouchDownX);
                if (!mIsBeingDragged && Math.abs(distanceX) > mTouchSlop) {
                    mLastX = mTouchMoveX;
                    mIsBeingDragged = true;
                }
                if (mIsBeingDragged)
                    if (getParent() != null) getParent().requestDisallowInterceptTouchEvent(true);

                if (mIsBeingDragged && mMinOffset < 0) {
                    float rawXMove = event.getX();
                    // 计算偏移量
                    float offsetX = rawXMove - mLastX;
                    // 在当前偏移量的基础上增加偏移量
                    mOffset -= offsetX;
                    refresh();
                    // 偏移量修改后下次重绘会有变化
                    mLastX = rawXMove;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    // 计算速度的单位时间,最大速度为5px/ms
                    velocityTracker.computeCurrentVelocity(1, 5);
                    float mVelocityX = velocityTracker.getXVelocity();
                    // 计算完成后回收内存
                    velocityTracker.clear();
                    //velocityTracker.recycle();
                    Log.e(TAG, "mVelocityX = " + mVelocityX + " px/ms");
                    //偏移量已经是边界时不用再计算滚动逻辑
                    if (mOffset > 0 && mOffset < mMaxOffset && mVelocityX != 0) {
                        float dis;//速度越大滚动距离理应越大,假设速度为5px/ms时，最大滑动位移3000px，设置花费时间为2000ms。以此为标准.速度越大，位移越大，时间越长。
                        int duration;
                        dis = mVelocityX / (5f / 3000f);
                        duration = (int) Math.abs(mVelocityX / (5f / 2000f));
                        Log.e(TAG, "dis = " + dis + " px" + "  duration = " + duration + " ms");
                        if (dis != 0) {
                            int endX = (int) (mOffset - dis);
                            Log.e(TAG, "duration--2 = " + duration);
                            //scroller.getCurrX() = mStartX + Math.round(scale * dx);  scale等于从0逐渐增大到1.
                            mScroller.startScroll((int) mOffset, 0, (int) -dis, 0, Math.max(duration, 500));//duration太小会有跳动效果，不平滑
                        }
                    }
                }
                mIsBeingDragged = false;
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            mOffset = mScroller.getCurrX();
            Log.e(TAG, "computeScroll = " + mOffset);
            refresh();
        } else {
            mScroller.forceFinished(true);
        }
    }

    /**
     * 对偏移量进行边界值判定
     */
    private void refresh() {
        if (mItemList.size() <= 1) {
            return;
        }
        mOffset = Math.min(Math.max(mOffset, 0), mMaxOffset);
        invalidate();
    }


    public static class HeartLineItem {
        public int yValue;
        public String xLabel;

        public HeartLineItem(int yValue, String xLabel) {
            this.yValue = yValue;
            this.xLabel = xLabel;
        }
    }
}
