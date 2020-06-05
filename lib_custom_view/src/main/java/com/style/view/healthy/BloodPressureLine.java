package com.style.view.healthy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 自定义部分滚动View
 */

public class BloodPressureLine extends View {
    private final String TAG = this.getClass().getSimpleName();

    private static final int COLOR_SBP = 0xFF45CE7B;
    private static final int COLOR_DBP = 0xFFF48D28;

    private static final int colorLabelY = 0xffaaaaaa;
    private static final int colorGrid = 0x99CCCCCC;
    private static final int colorChartText = 0xff666666;
    private TextPaint mLabelYPaint;
    private TextPaint mLabelXPaint;
    private Paint mSBPPaint;
    private Paint mDBPPaint;
    private TextPaint mValuePaint;
    private int lineWidth = 2;
    private int defaultWidth;
    //X轴坐标间隔宽度
    private int xIntervalWidth = 60;
    //X轴起始偏移宽度
    private int xStartOffset = 20;
    private int xEndOffset = 20;
    //Y轴顶端距离曲线布局高度
    private float yStartOffset = 20f;
    private float yEndOffset = 40f;
    private Path mLinePath;
    private Path mLinePath2;

    /**
     * 坐标文本高度
     */
    private float labelXHeight, labelYHeight;
    private int mViewHeight, mViewWidth;
    /**
     * 网格宽高
     */
    private float mYaxisHeight;
    /**
     * 纵坐标文本高度
     */
    private float mYTextWidth;

    private ArrayList<BloodItem> mItemList = new ArrayList<>();
    private static final float DEFAULT_MIN = 40F;
    private static final float DEFAULT_MAX = 160F;
    private static final float DEFAULT_MIN_SECOND = 0F;
    private static final float DEFAULT_MAX_SECOND = 200F;
    private float yMin = DEFAULT_MIN;
    private float yMax = DEFAULT_MAX;

    public BloodPressureLine(Context context) {
        this(context, null);
    }

    public BloodPressureLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BloodPressureLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultWidth = dp2px(getContext(), 300);
        mViewWidth = defaultWidth;
        xIntervalWidth = dp2px(getContext(), (int) xIntervalWidth);
        xStartOffset = dp2px(getContext(), xStartOffset);
        xEndOffset = dp2px(getContext(), xEndOffset);
        yStartOffset = dp2px(getContext(), (int) yStartOffset);
        yEndOffset = dp2px(getContext(), (int) yEndOffset);
        lineWidth = dp2px(getContext(), lineWidth);
        mLinePath = new Path();
        mLinePath2 = new Path();

        init(context);
        //setData(getData());
    }

    private void init(Context context) {
        mLabelYPaint = new TextPaint();
        mLabelYPaint.setAntiAlias(true);
        mLabelYPaint.setTextAlign(Paint.Align.CENTER);
        mLabelYPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        mLabelYPaint.setColor(colorLabelY);
        labelYHeight = mLabelYPaint.getFontMetrics().bottom - mLabelYPaint.getFontMetrics().top;
        mYTextWidth = mLabelYPaint.measureText("200");

        mLabelXPaint = new TextPaint(mLabelYPaint);
        labelXHeight = mLabelXPaint.getFontMetrics().bottom - mLabelXPaint.getFontMetrics().top;

        mValuePaint = new TextPaint(mLabelYPaint);
        mValuePaint.setColor(colorChartText);

        mSBPPaint = new Paint();
        mSBPPaint.setAntiAlias(true);
        mSBPPaint.setStyle(Paint.Style.STROKE);
        mSBPPaint.setStrokeJoin(Paint.Join.ROUND);// 笔刷图形样式
        mSBPPaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔转弯的连接风格
        mSBPPaint.setDither(true);//防抖动
        mSBPPaint.setShader(null);//设置渐变色
        mSBPPaint.setStrokeWidth(lineWidth);
        mSBPPaint.setColor(COLOR_SBP);

        mDBPPaint = new Paint(mSBPPaint);
        mDBPPaint.setColor(COLOR_DBP);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        mYaxisHeight = mViewHeight - yStartOffset - yEndOffset;
        Log.e(TAG, "onMeasure--" + mViewWidth + "  " + mViewHeight);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPolyAndXLabel(canvas);
    }

    private void drawPolyAndXLabel(Canvas canvas) {
        if (mItemList == null || mItemList.isEmpty()) {
            //drawEmpty(canvas);
            return;
        }
        mLinePath.reset();
        mLinePath2.reset();
        canvas.save();
        canvas.translate(xStartOffset, yStartOffset + mYaxisHeight);

        BloodItem item;
        //LinearGradient gradient;
        for (int i = 0; i < mItemList.size(); i++) {
            item = mItemList.get(i);
            float x = xIntervalWidth * i;
            float y = -mYaxisHeight * (item.yHigh - yMin) / (yMax - yMin);
            float y2 = -mYaxisHeight * (item.yLow - yMin) / (yMax - yMin);
            canvas.drawText(item.xLabel, x, yEndOffset / 2 + getBaseLine2CenterY(mLabelXPaint.getFontMetrics()), mLabelXPaint);
            mSBPPaint.setStyle(Paint.Style.FILL);
            mDBPPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(x, y, lineWidth, mSBPPaint);
            canvas.drawCircle(x, y2, lineWidth, mDBPPaint);

            if (i == 0) {
                mLinePath.moveTo(x, y);
                mLinePath2.moveTo(x, y2);
            } else {
                mLinePath.lineTo(x, y);
                mLinePath2.lineTo(x, y2);
            }
        }
        mSBPPaint.setStyle(Paint.Style.STROKE);
        mDBPPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mLinePath, mSBPPaint);
        canvas.drawPath(mLinePath2, mDBPPaint);
        canvas.restore();
    }

    public void setData(List<BloodItem> list) {
        mViewWidth = defaultWidth;

        mItemList.clear();
        yMin = DEFAULT_MIN;
        yMax = DEFAULT_MAX;
        if (list != null && !list.isEmpty()) {
            for (BloodItem item : list) {
                if (item.yHigh > DEFAULT_MAX || item.yLow < DEFAULT_MIN) {
                    yMax = DEFAULT_MAX_SECOND;
                    yMin = DEFAULT_MIN_SECOND;
                }
            }
            mItemList.addAll(list);
            mViewWidth = xIntervalWidth * (mItemList.size() - 1) + xStartOffset + xEndOffset;
        }
        requestLayout();
        invalidate();
    }

    private List<BloodItem> getData() {
        ArrayList<BloodItem> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            BloodItem b = new BloodItem(random.nextInt(30) + 50, random.nextInt(30) + 90, String.valueOf(i));
            list.add(b);
        }
        return list;
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

    public float getTextHeight(Paint.FontMetrics fontMetrics) {
        return Math.abs(fontMetrics.descent - fontMetrics.ascent);
    }

    public static class BloodItem {
        public int yLow;
        public int yHigh;
        public String xLabel;

        public BloodItem(int yLow, int yHigh, String xLabel) {
            this.yLow = yLow;
            this.yHigh = yHigh;
            this.xLabel = xLabel;
        }
    }

    public static int dp2px(Context context, float dpValue) {
        float pxValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
        return (int) (pxValue + 0.5f);//0.5f是为了四舍五入，但有时候四舍五入不一定就好
    }

    public static float sp2px(Context context, int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
