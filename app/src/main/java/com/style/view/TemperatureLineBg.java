package com.style.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.style.view.util.Utils;

import java.text.DecimalFormat;

/**
 * 心率历史曲线图
 */

public class TemperatureLineBg extends View {
    //y轴刻度画笔
    private Paint mAxisPaint;
    //虚线画笔
    private Paint mDashPathPaint;
    //大刻度线画笔刻度
    private float yBigAxisStrokeWidth = 1.5f;
    //小刻度线画笔刻度
    private float ySmallAxisStrokeWidth = 1.0f;
    //y轴温度值画笔
    private TextPaint mLabelYPaint;

    private float mHeight, mWidth;
    private float yMaxValue = 38.0f;
    private float yMinValue = 35.0f;

    //X轴起始偏移宽度
    private float xStartOffset = 0;
    //X轴结束偏移宽度
    private float xEndOffset = 0;
    //Y轴起始偏移宽度
    private float yStartOffset = 40;
    //Y轴结束偏移宽度
    private float yEndOffset = 40;
    //Y大刻度线宽度
    private float yBigNumberLineWidth = 15;
    //Y小刻度线宽度
    private float ySmallNumberLineWidth = 8;
    private float averageValue;

    public TemperatureLineBg(Context context) {
        this(context, null);
    }

    public TemperatureLineBg(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TemperatureLineBg(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        xStartOffset = Utils.dp2px(getContext(), (int) xStartOffset);
        yStartOffset = Utils.dp2px(getContext(), (int) yStartOffset);
        yEndOffset = Utils.dp2px(getContext(), (int) yEndOffset);

        yBigNumberLineWidth = Utils.dp2px(getContext(), (int) yBigNumberLineWidth);
        ySmallNumberLineWidth = Utils.dp2px(getContext(), (int) ySmallNumberLineWidth);
        yBigAxisStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, yBigAxisStrokeWidth, getResources().getDisplayMetrics());
        ySmallAxisStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ySmallAxisStrokeWidth, getResources().getDisplayMetrics());

        setMaxAndMin(yMaxValue, yMinValue);
        init();

    }

    public void setMaxAndMin(float max, float min) {
        this.yMaxValue = (float) (Math.ceil(max) < yMaxValue ? yMaxValue : Math.ceil(max));
        this.yMinValue = (float) (Math.floor(min) > yMinValue ? yMinValue : Math.floor(min));
    }

    private void init() {
        mAxisPaint = new Paint();
        mAxisPaint.setAntiAlias(true);
        mAxisPaint.setStrokeJoin(Paint.Join.ROUND);// 笔刷图形样式
        mAxisPaint.setDither(true);//防抖动
        mAxisPaint.setShader(null);
        mAxisPaint.setStrokeWidth(ySmallAxisStrokeWidth);
        mAxisPaint.setColor(0xFFFFFFFF);

        mLabelYPaint = new TextPaint();
        mLabelYPaint.setAntiAlias(true);
        mLabelYPaint.setColor(0xFFFFFFFF);
        mLabelYPaint.setTextAlign(Paint.Align.CENTER);
        mLabelYPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, getResources().getDisplayMetrics()));

        mDashPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDashPathPaint.setDither(true);//防抖动
        mDashPathPaint.setShader(null);
        mDashPathPaint.setStyle(Paint.Style.STROKE);
        mDashPathPaint.setColor(0xFFFFFFFF);
        mDashPathPaint.setStrokeWidth(ySmallAxisStrokeWidth);
        DashPathEffect pathEffect = new DashPathEffect(new float[]{16, 16}, 0);
        mDashPathPaint.setPathEffect(pathEffect);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension((int) mWidth, (int) mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawGridsAndYLabel(canvas);
    }

    private void drawGridsAndYLabel(Canvas canvas) {
        mAxisPaint.setStrokeWidth(yBigAxisStrokeWidth);
        //画Y轴
        canvas.drawLine(xStartOffset, 0, xStartOffset, mHeight - yStartOffset, mAxisPaint);
        //画X轴
        float endX = mWidth - xStartOffset;
        canvas.drawLine(xStartOffset, mHeight - yStartOffset, endX, mHeight - yStartOffset, mAxisPaint);

        float yTextLength = mLabelYPaint.measureText("40");
        float ascent = mLabelYPaint.ascent();
        float descent = mLabelYPaint.descent();
        int yLabel = (int) yMinValue;
        float yValueAllHeight = (mHeight - yStartOffset - yEndOffset);
        int yAxisCount = (int) ((yMaxValue - yMinValue) * 5);
        for (int i = 0; i <= yAxisCount; i++) {
            float y = yValueAllHeight - yValueAllHeight * (0.2f * i) / (yMaxValue - yMinValue) + yStartOffset;
            if (i % 5 == 0) {
                mAxisPaint.setStrokeWidth(yBigAxisStrokeWidth);
                canvas.drawLine(xStartOffset, y, xStartOffset + yBigNumberLineWidth, y, mAxisPaint);
                if (i > 0)
                    canvas.drawText(String.valueOf(yLabel), xStartOffset + yBigNumberLineWidth + yTextLength, y + descent, mLabelYPaint);
                yLabel++;
            } else {
                mAxisPaint.setStrokeWidth(ySmallAxisStrokeWidth);
                canvas.drawLine(xStartOffset, y, xStartOffset + ySmallNumberLineWidth, y, mAxisPaint);
            }
        }

        if (averageValue > 0) {
            //取小数点后一位
            String average = new DecimalFormat("0.0").format(averageValue);
            averageValue = Float.valueOf(average);
            float y = yValueAllHeight - yValueAllHeight * (averageValue - yMinValue) / (yMaxValue - yMinValue) + yStartOffset;
            canvas.drawLine(xStartOffset + yBigNumberLineWidth * 3, y, endX - yBigNumberLineWidth * 2, y, mDashPathPaint);

            String text = String.format("平均温度", average);
            float averageLength = mLabelYPaint.measureText(text);
            float textHeight = mLabelYPaint.getFontMetrics().bottom - mLabelYPaint.getFontMetrics().top;
            canvas.drawText(text, xStartOffset + yBigNumberLineWidth * 3 + averageLength / 2, y + ascent, mLabelYPaint);

        }
    }

    public void setAverageValue(float v) {
        this.averageValue = v;
        requestLayout();
        invalidate();
    }
}
