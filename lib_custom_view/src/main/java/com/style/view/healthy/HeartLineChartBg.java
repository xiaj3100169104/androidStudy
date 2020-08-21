package com.style.view.healthy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 自定义部分滚动View
 */

public class HeartLineChartBg extends View {
    private final String TAG = this.getClass().getSimpleName();

    private Paint mAxisPaint;
    private TextPaint mLabelXPaint;
    private TextPaint mLabelYPaint;
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
    private static final float DEFAULT_MIN = 40F;
    private static final float DEFAULT_MAX = 120F;
    private static final float DEFAULT_MIN_SECOND = 0F;
    private static final float DEFAULT_MAX_SECOND = 200F;
    private float yMin = DEFAULT_MIN;
    private float yMax = DEFAULT_MAX;
    private ArrayList<HeartLineChart.HeartLineItem> mItemList = new ArrayList<>();

    public HeartLineChartBg(Context context) {
        this(context, null);
    }

    public HeartLineChartBg(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartLineChartBg(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaddingTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPaddingTop, getResources().getDisplayMetrics());
        mPaddingBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPaddingBottom, getResources().getDisplayMetrics());
        mPaddingLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPaddingLeft, getResources().getDisplayMetrics());
        mPaddingRight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPaddingRight, getResources().getDisplayMetrics());
        initPaint(context);
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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        mXaxisWidth = mViewWidth - mPaddingLeft - mPaddingRight;
        mYaxisHeight = mViewHeight - mPaddingTop - mPaddingBottom;
        Log.e(TAG, "onMeasure--" + mViewWidth + "  " + mViewHeight);
        setMeasuredDimension(mViewWidth, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawGridsAndYLabel(canvas);
    }

    private void drawGridsAndYLabel(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.save();
        canvas.translate(mPaddingLeft, mPaddingTop);
        //画竖线
        float xGridWidth = mXaxisWidth / 4;
        for (int i = 0; i <= 4; i++) {
            canvas.drawLine(xGridWidth * i, 0, xGridWidth * i, mYaxisHeight, mAxisPaint);
        }
        //画横线
        float yGridHeight = mYaxisHeight / 4;
        for (int i = 0; i <= 4; i++) {
            int y = (int) (yGridHeight * i);
            canvas.drawLine(0, y, mXaxisWidth, y, mAxisPaint);
            int yLabel = (int) (yMax - (yMax - yMin) / 4 * i);
            canvas.drawText(String.valueOf(yLabel), -mPaddingLeft / 2, y + labelYHeight / 3, mLabelYPaint);
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

    public void setData(List<HeartLineChart.HeartLineItem> list) {
        mItemList.clear();
        yMin = DEFAULT_MIN;
        yMax = DEFAULT_MAX;
        if (list != null && !list.isEmpty()) {
            for (HeartLineChart.HeartLineItem item : list) {
                if (item.yValue > DEFAULT_MAX || item.yValue < DEFAULT_MIN) {
                    yMax = DEFAULT_MAX_SECOND;
                    yMin = DEFAULT_MIN_SECOND;
                }
            }
            mItemList.addAll(list);
        }
        requestLayout();
        invalidate();
    }
}
