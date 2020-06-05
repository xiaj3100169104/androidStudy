package com.style.view.healthy;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 自定义部分滚动View
 */

public class GradeTrendView extends View {
    private final String TAG = this.getClass().getSimpleName();

    //图表线宽度
    private int lineWidth = 3;
    private Path mLinePath;

    private Paint mChartPaint;
    private Paint mAxisPaint;
    private TextPaint mLabelXPaint;
    private TextPaint mLabelYPaint;

    //图表线颜色
    private int lineColor = 0xFF45CE7B;
    private int colorLabelY = 0xffaaaaaa;
    private int colorGrid = 0x99CCCCCC;
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
     * 起点到网格左边界的距离
     */
    private float mStartPointXoffset = 30f;
    /**
     * 终点到网格右边界的距离
     */
    private float mEndPointXoffset = 30f;
    /**
     * 柱子间间隔宽度
     */
    private float mIntervalWidth;
    private ArrayList<PointItem> mItemList = new ArrayList<>();
    private static final float DEFAULT_MIN = 0F;
    private static final float DEFAULT_MAX = 100F;
    private float yMin = DEFAULT_MIN;
    private float yMax = DEFAULT_MAX;

    public GradeTrendView(Context context) {
        this(context, null);
    }

    public GradeTrendView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradeTrendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        lineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, lineWidth, getResources().getDisplayMetrics());
        mLinePath = new Path();
        initPaint(context);
        setData(getData());
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
        canvas.translate(mPadding + mYTextWidth, mPadding);
        //画竖线
        float xGridWidth = mXaxisWidth / 4;
        for (int i = 0; i <= 4; i++) {
            canvas.drawLine(xGridWidth * i, 0, xGridWidth * i, mYaxisHeight, mAxisPaint);
        }
        //画横线
        float yGridHeight = mYaxisHeight / 5;
        for (int i = 0; i <= 5; i++) {
            int y = (int) (yGridHeight * i);
            canvas.drawLine(0, y, mXaxisWidth, y, mAxisPaint);
            int yLabel = (int) (yMax - (yMax - yMin) / 5 * i);
            canvas.drawText(String.valueOf(yLabel), -mYTextWidth, y + labelYHeight / 3, mLabelYPaint);
        }
        canvas.restore();
    }

    private void drawPolyAndXLabel(Canvas canvas) {
        if (mItemList == null || mItemList.isEmpty() || mItemList.size() == 1) {
            return;
        }
        canvas.save();
        canvas.translate(mPadding + mYTextWidth, mPadding + mYaxisHeight);
        mIntervalWidth = (mXaxisWidth - mStartPointXoffset - mEndPointXoffset) / 6;
        PointItem item;
        //画曲线
        mChartPaint.setStyle(Paint.Style.STROKE);
        mChartPaint.setColor(lineColor);
        mChartPaint.setStrokeWidth(lineWidth);
        for (int i = 0; i < mItemList.size(); i++) {
            item = mItemList.get(i);
            float x = mStartPointXoffset + mIntervalWidth * i;
            float y = -mYaxisHeight * (item.yValue - yMin) / (yMax - yMin);
            canvas.drawCircle(x, y, lineWidth, mChartPaint);

            if (i == 0) {
                mLinePath.moveTo(x, y);
            } else {
                mLinePath.lineTo(x, y);
            }
        }
        canvas.drawPath(mLinePath, mChartPaint);
        //画点、x坐标
        for (int i = 0; i < mItemList.size(); i++) {
            item = mItemList.get(i);
            float x = mStartPointXoffset + mIntervalWidth * i;
            float y = -mYaxisHeight * (item.yValue - yMin) / (yMax - yMin);
            //画x坐标
            canvas.drawText(item.xLabel, x, (labelYHeight + mPadding) / 2 + getBaseLine2CenterY(mLabelXPaint.getFontMetrics()), mLabelXPaint);
            //画内圆
            mChartPaint.setStyle(Paint.Style.FILL);
            mChartPaint.setColor(0xffffffff);
            canvas.drawCircle(x, y, lineWidth, mChartPaint);
            //画外圆
            mChartPaint.setStyle(Paint.Style.STROKE);
            mChartPaint.setColor(lineColor);
            mChartPaint.setStrokeWidth(1);
            canvas.drawCircle(x, y, lineWidth, mChartPaint);

        }
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

    public void setData(List<PointItem> list) {
        mItemList.clear();
        mItemList.addAll(list);
        //requestLayout();
        //invalidate();
    }

    private List<PointItem> getData() {
        ArrayList<PointItem> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            PointItem b = new PointItem(random.nextInt(60) + 40, String.valueOf(i));
            list.add(b);
        }
        return list;
    }

    public static class PointItem {
        public int yValue;
        public String xLabel;

        public PointItem(int yValue, String xLabel) {
            this.yValue = yValue;
            this.xLabel = xLabel;
        }
    }

    private float mTouchDownX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = event.getX();
                if (mItemList.size() > 0) {
                    mSpot = getTouchSpot(mTouchDownX);
                    invalidate();
                }
                break;
        }
        return true;
    }

    private int mSpot = -1;

    private int getTouchSpot(float mTouchDownX) {
        float mStartX = mPadding + mYTextWidth + mStartPointXoffset;
        float mEndX = mViewWidth - mPadding - mEndPointXoffset;
        int index = -1;
        if (mTouchDownX < mStartX - mIntervalWidth / 2 || mTouchDownX > mEndX + mIntervalWidth / 2)
            return index;
        for (int i = 0; i < mItemList.size(); i++) {
            float left = mStartX + mIntervalWidth * i - mIntervalWidth / 2;
            float right = left + mIntervalWidth;
            if (mTouchDownX >= left && mTouchDownX < right) {
                index = i;
                break;
            }
        }
        Log.e("getTouchSpot", index + "");
        return index;
    }
}
