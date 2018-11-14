package com.style.view.healthy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.style.utils.DeviceInfoUtil;

/**
 * 心率历史曲线图
 */

public class TemperatureLineBg extends View {
    //y轴刻度画笔
    private Paint mAxisPaint;
    private int colorChart = 0xFF45CE7B;
    private int colorLabelY = 0xffaaaaaa;
    private int colorGrid = 0xFF45CE7B;
    //虚线画笔
    private Paint mDashPathPaint;
    //大刻度线画笔刻度
    private float yBigAxisStrokeWidth = 1.5f;
    //小刻度线画笔刻度
    private float ySmallAxisStrokeWidth = 1.0f;
    //y轴温度值画笔
    private TextPaint mLabelYPaint;

    private float mHeight, mWidth;
    private static final float DEFAULT_MIN = 35.0F;
    private static final float DEFAULT_MAX = 38.0F;
    private float yMaxValue = DEFAULT_MAX;
    private float yMinValue = DEFAULT_MIN;

    //X轴起始偏移宽度
    private float xStartOffset = 40;
    //X轴结束偏移宽度
    private float xEndOffset = 20;
    //Y轴起始偏移宽度
    private float yStartOffset = 20;
    //Y轴结束偏移宽度
    private float yEndOffset = 40;
    //Y大刻度线宽度
    private float yBigNumberLineWidth = 15;
    //Y小刻度线宽度
    private float ySmallNumberLineWidth = 8;
    private float averageValue;
    private float yValueAllHeight;
    private float xValueAllWidth;

    public TemperatureLineBg(Context context) {
        this(context, null);
    }

    public TemperatureLineBg(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TemperatureLineBg(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        xStartOffset = DeviceInfoUtil.dp2px(getContext(), (int) xStartOffset);
        xEndOffset = DeviceInfoUtil.dp2px(getContext(), (int) xEndOffset);
        yStartOffset = DeviceInfoUtil.dp2px(getContext(), (int) yStartOffset);
        yEndOffset = DeviceInfoUtil.dp2px(getContext(), (int) yEndOffset);

        yBigNumberLineWidth = DeviceInfoUtil.dp2px(getContext(), (int) yBigNumberLineWidth);
        ySmallNumberLineWidth = DeviceInfoUtil.dp2px(getContext(), (int) ySmallNumberLineWidth);
        yBigAxisStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, yBigAxisStrokeWidth, getResources().getDisplayMetrics());
        ySmallAxisStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ySmallAxisStrokeWidth, getResources().getDisplayMetrics());
        init();
    }

    private void init() {
        mAxisPaint = new Paint();
        mAxisPaint.setAntiAlias(true);
        mAxisPaint.setStrokeJoin(Paint.Join.ROUND);// 笔刷图形样式
        mAxisPaint.setDither(true);//防抖动
        mAxisPaint.setShader(null);
        mAxisPaint.setStrokeWidth(ySmallAxisStrokeWidth);
        mAxisPaint.setColor(colorGrid);

        mLabelYPaint = new TextPaint();
        mLabelYPaint.setAntiAlias(true);
        mLabelYPaint.setColor(colorLabelY);
        mLabelYPaint.setTextAlign(Paint.Align.CENTER);
        mLabelYPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, getResources().getDisplayMetrics()));

        mDashPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDashPathPaint.setDither(true);//防抖动
        mDashPathPaint.setShader(null);
        mDashPathPaint.setStyle(Paint.Style.STROKE);
        mDashPathPaint.setColor(colorChart);
        mDashPathPaint.setStrokeWidth(ySmallAxisStrokeWidth);
        DashPathEffect pathEffect = new DashPathEffect(new float[]{16, 16}, 0);
        mDashPathPaint.setPathEffect(pathEffect);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        xValueAllWidth = mWidth - xStartOffset - xEndOffset;
        yValueAllHeight = mHeight - yStartOffset - yEndOffset;
        setMeasuredDimension((int) mWidth, (int) mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawGridsAndYLabel(canvas);
    }

    private void drawGridsAndYLabel(Canvas canvas) {
        mAxisPaint.setStrokeWidth(yBigAxisStrokeWidth);
        //画Y轴
        canvas.translate(xStartOffset, yStartOffset + yValueAllHeight);
        canvas.drawLine(0, 0, 0, -yValueAllHeight, mAxisPaint);
        canvas.drawLine(xValueAllWidth, 0, xValueAllWidth, -yValueAllHeight, mAxisPaint);
        //画X轴
        float endX = mWidth - xStartOffset;
        canvas.drawLine(0, 0, xValueAllWidth, 0, mAxisPaint);

        float yTextWidth = mLabelYPaint.measureText("40");
        float yLabelHeight = mLabelYPaint.getFontMetrics().bottom - mLabelYPaint.getFontMetrics().top;

        mAxisPaint.setStrokeWidth(ySmallAxisStrokeWidth);
        int yLabel = (int) yMinValue;
        for (float i = yMinValue; i <= yMaxValue; i++) {
            //float y = yValueAllHeight - yValueAllHeight * (0.2f * i) / (yMaxValue - yMinValue) + yStartOffset;
            float y = -yValueAllHeight * (i - yMinValue) / (yMaxValue - yMinValue);
            //第一条刻度线不画
            if (i > yMinValue)
                canvas.drawLine(0, y, -20, y, mAxisPaint);

            canvas.drawText(String.valueOf((int) i), -20 - yTextWidth / 2, y + yLabelHeight / 3, mLabelYPaint);
        }
        canvas.restore();

      /*  if (averageValue > 0) {
            //取小数点后一位
            String average = new DecimalFormat("0.0").format(averageValue);
            averageValue = Float.valueOf(average);
            float y = yValueAllHeight - yValueAllHeight * (averageValue - yMinValue) / (yMaxValue - yMinValue) + yStartOffset;
            canvas.drawLine(xStartOffset + yBigNumberLineWidth * 3, y, endX - yBigNumberLineWidth * 2, y, mDashPathPaint);

            String text = String.format(getContext().getString(R.string.average_temp), average);
            float averageLength = mLabelYPaint.measureText(text);
            float textHeight = mLabelYPaint.getFontMetrics().bottom - mLabelYPaint.getFontMetrics().top;
            canvas.drawText(text, xStartOffset + yBigNumberLineWidth * 3 + averageLength / 2, y + ascent, mLabelYPaint);

        }*/
    }

    public void setMaxAndMin(float max, float min) {
        this.yMaxValue = (float) (Math.ceil(max) < yMaxValue ? yMaxValue : Math.ceil(max));
        this.yMinValue = (float) (Math.floor(min) > yMinValue ? yMinValue : Math.floor(min));
        requestLayout();
        invalidate();
    }

    public void averageValue(float v) {
        this.averageValue = v;
        requestLayout();
        invalidate();
    }

    public void reset() {
        this.yMinValue = DEFAULT_MIN;
        this.yMaxValue = DEFAULT_MAX;
        requestLayout();
        invalidate();
    }
}
