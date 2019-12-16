package com.style.view.healthy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

/**
 * 自定义部分滚动View
 */

public class BloodPressureLineBg extends View {
    private final String TAG = this.getClass().getSimpleName();

    private static final int colorLabelY = 0xffaaaaaa;
    private static final int colorGrid = 0x99CCCCCC;
    private static final int colorChartText = 0xff666666;
    private Paint mAxisPaint;
    private TextPaint mLabelYPaint;
    private int lineWidth = 2;

    private int mViewHeight, mViewWidth;

    //X轴起始偏移宽度
    private float xStartOffset = 40;
    //X轴结束偏移宽度
    private float xEndOffset = 20;
    //Y轴起始偏移宽度
    private float yStartOffset = 20;
    //Y轴结束偏移宽度
    private float yEndOffset = 40;
    /**
     * 网格宽高
     */
    private float mYaxisHeight, mXaxisWidth;
    /**
     * 纵坐标文本高度
     */
    private float mYTextWidth;

    private static final float DEFAULT_MIN = 40F;
    private static final float DEFAULT_MAX = 160F;
    private static final float DEFAULT_MIN_SECOND = 0F;
    private static final float DEFAULT_MAX_SECOND = 200F;
    private float yMin = DEFAULT_MIN;
    private float yMax = DEFAULT_MAX;

    public BloodPressureLineBg(Context context) {
        this(context, null);
    }

    public BloodPressureLineBg(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BloodPressureLineBg(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        xStartOffset = dp2px(getContext(), (int) xStartOffset);
        xEndOffset = dp2px(getContext(), (int) xEndOffset);
        yStartOffset = dp2px(getContext(), (int) yStartOffset);
        yEndOffset = dp2px(getContext(), (int) yEndOffset);
        lineWidth = dp2px(getContext(), lineWidth);
        init(context);
        //setData(getData());
    }

    private void init(Context context) {
        mAxisPaint = new Paint();
        mAxisPaint.setAntiAlias(true);
        mAxisPaint.setStyle(Paint.Style.STROKE);
        mAxisPaint.setDither(true);//防抖动
        mAxisPaint.setShader(null);//设置渐变色
        mAxisPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics()));
        mAxisPaint.setColor(colorGrid);

        mLabelYPaint = new TextPaint();
        mLabelYPaint.setAntiAlias(true);
        mLabelYPaint.setTextAlign(Paint.Align.CENTER);
        mLabelYPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        mLabelYPaint.setColor(colorLabelY);
        mYTextWidth = mLabelYPaint.measureText("200");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        mXaxisWidth = mViewWidth - xStartOffset - xEndOffset;
        mYaxisHeight = mViewHeight - yStartOffset - yEndOffset;
        Log.e(TAG, "onMeasure--" + mViewWidth + "  " + mViewHeight);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGridsAndYLabel(canvas);
    }

    private void drawGridsAndYLabel(Canvas canvas) {
        canvas.save();
        canvas.translate(xStartOffset, yStartOffset + mYaxisHeight);
        //画竖线
        float xGridWidth = mXaxisWidth / 4;
        for (int i = 0; i <= 4; i++) {
            canvas.drawLine(xGridWidth * i, 0, xGridWidth * i, -mYaxisHeight, mAxisPaint);
        }
        //画横线
        for (float i = yMin; i <= yMax; i += 40) {
            //float y = yValueAllHeight - yValueAllHeight * (0.2f * i) / (yMaxValue - yMinValue) + yStartOffset;
            float y = -mYaxisHeight * (i - yMin) / (yMax - yMin);
            canvas.drawLine(0, y, mXaxisWidth, y, mAxisPaint);
            canvas.drawText(String.valueOf((int) i), -mYTextWidth, y + getBaseLine2CenterY(mLabelYPaint.getFontMetrics()), mLabelYPaint);
        }
        canvas.restore();
    }

    public void setData(List<BloodPressureLine.BloodItem> list) {
        yMin = DEFAULT_MIN;
        yMax = DEFAULT_MAX;
        if (list != null && !list.isEmpty()) {
            for (BloodPressureLine.BloodItem item : list) {
                if (item.yHigh > DEFAULT_MAX || item.yLow < DEFAULT_MIN) {
                    yMax = DEFAULT_MAX_SECOND;
                    yMin = DEFAULT_MIN_SECOND;
                }
            }
        }
        requestLayout();
        invalidate();
    }

    public void reset() {
        this.yMin = DEFAULT_MIN;
        this.yMax = DEFAULT_MAX;
        requestLayout();
        invalidate();
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

    public static int dp2px(Context context, float dpValue) {
        float pxValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
        return (int) (pxValue + 0.5f);//0.5f是为了四舍五入，但有时候四舍五入不一定就好
    }

    public static float sp2px(Context context, int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
