package com.style.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.style.view.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 心率历史曲线图
 */

public class HeartRateLineView extends View {
    private final String TAG = this.getClass().getSimpleName();

    //横向和纵向分割线
    private Paint mAxisPaint = new Paint();
    //图表的坐标点的值的paint
    private TextPaint mLabelYPaint = new TextPaint();

    private float mHeight, mWidth;
    //X轴起始偏移宽度
    private float xStartOffset = 40;
    private float xEndOffset = 40;
    //Y轴起始偏移宽度
    private float yStartOffset = 40;
    private float yEndOffset = 40;
    private float yMaxValue = 150f;
    private float yMinValue = 0f;

    //X轴坐标间隔宽度
    private int xLabelIntervalWidth = 20;
    private ArrayList<PointItem> dataList = new ArrayList<>();
    private float xDown;
    private float xMove;
    private float xMoveLast;
    private float xAllTranslation;
    private float xTranslation;

    public HeartRateLineView(Context context) {
        this(context, null);
    }

    public HeartRateLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartRateLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        xStartOffset = Utils.dp2px(getContext(), (int) xStartOffset);
        xEndOffset = Utils.dp2px(getContext(), (int) xEndOffset);
        yEndOffset = Utils.dp2px(getContext(), (int) yEndOffset);
        yStartOffset = Utils.dp2px(getContext(), (int) yStartOffset);
        xLabelIntervalWidth = Utils.dp2px(getContext(), xLabelIntervalWidth);

        init();
    }

    private void init() {
        mAxisPaint.setAntiAlias(true);
        mAxisPaint.setStyle(Paint.Style.STROKE);
        mAxisPaint.setStrokeJoin(Paint.Join.ROUND);// 笔刷图形样式
        mAxisPaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔转弯的连接风格
        mAxisPaint.setDither(true);//防抖动
        mAxisPaint.setShader(null);
        mAxisPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics()));
        mAxisPaint.setColor(0x99CCCCCC);

        mLabelYPaint.setAntiAlias(true);
        mLabelYPaint.setColor(0xffcccccc);
        mLabelYPaint.setTextAlign(Paint.Align.CENTER);
        mLabelYPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));

    }

    public void setData(List<PointItem> list) {
        dataList.clear();
        if (list != null && !list.isEmpty()) {
            dataList.addAll(list);
        }
        invalidate();
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
        drawLineAndXLabel(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getX();
                xTranslation = xMove - xDown;
                xAllTranslation += xTranslation;
                Log.e(TAG, "xAllTranslation  " + xAllTranslation);
                invalidate();
                //xMoveLast = xMove;
                xAllTranslation -= xTranslation;
                break;
            case MotionEvent.ACTION_UP:
                xAllTranslation += xTranslation;
                break;
        }
        return true;
    }

    protected void drawLineAndXLabel(Canvas canvas) {
        //mLinePath.reset();
        //mLineNoDataPath.reset();

        float yValueAllHeight = mHeight - yStartOffset - yEndOffset;
        float textLength = mLabelYPaint.measureText("00:00");
        float descent = mLabelYPaint.descent();
        float textHeight = mLabelYPaint.getFontMetrics().bottom - mLabelYPaint.getFontMetrics().top;

        boolean isNoDataStart = false;
        float xNoDataStart = 0;
        float yNoDataStart = 0;
        float xNoDataEnd;
        float yNoDataEnd;
        canvas.save();
        canvas.translate(xAllTranslation, 0);
        for (int i = 0; i < dataList.size(); i++) {
            PointItem item = dataList.get(i);
            String xLabel = item.xLabel;
            float yValue = item.yValue;
            float x = xLabelIntervalWidth * i + xStartOffset;
            float y = yValueAllHeight - yValueAllHeight * (yValue - yMinValue) / (yMaxValue - yMinValue) + yStartOffset;
            //Log.e(TAG, "x=" + x + "   y=" + y);
            //画x轴标签,间隔5分钟
            if (i % 5 == 0) {
                Log.e(TAG, "i--" + i + "  x--" + x);
                canvas.drawText(xLabel, x, mHeight - yStartOffset + textHeight, mLabelYPaint);
            }

        }
        canvas.restore();
    }

    private void drawGridsAndYLabel(Canvas canvas) {
        float xGridWidth = (mWidth - xStartOffset - xEndOffset) / 4;
        float startX = xStartOffset;
        //画竖线
        for (int i = 0; i < 5; i++) {
            canvas.drawLine(startX, yStartOffset, startX, mHeight - yStartOffset, mAxisPaint);
            startX += xGridWidth;
        }
        //画横线
        float yValueAllHeight = mHeight - yStartOffset - yEndOffset;

        float endX = mWidth - xStartOffset;
        float y1 = yStartOffset;
        canvas.drawLine(xStartOffset, y1, endX, y1, mAxisPaint);
        float y2 = yValueAllHeight - yValueAllHeight * (90f - yMinValue) / (yMaxValue - yMinValue) + yStartOffset;
        canvas.drawLine(xStartOffset, y2, endX, y2, mAxisPaint);
        float y3 = yValueAllHeight - yValueAllHeight * (60f - yMinValue) / (yMaxValue - yMinValue) + yStartOffset;
        canvas.drawLine(xStartOffset, y3, endX, y3, mAxisPaint);
        float y4 = mHeight - yStartOffset;
        canvas.drawLine(xStartOffset, y4, endX, y4, mAxisPaint);

        float textLength150 = mLabelYPaint.measureText("150");
        float ascent = mLabelYPaint.ascent();
        float descent = mLabelYPaint.descent();
        //float textHeight = mLabelYPaint.getFontMetrics().bottom - mLabelYPaint.getFontMetrics().top;
        canvas.drawText("150", xStartOffset - textLength150, y1 + descent, mLabelYPaint);
        float textLength90 = mLabelYPaint.measureText("90");
        canvas.drawText("90", xStartOffset - textLength90, y2 + descent, mLabelYPaint);
        float textLength60 = mLabelYPaint.measureText("60");
        canvas.drawText("60", xStartOffset - textLength60, y3 + descent, mLabelYPaint);
        canvas.drawText("0", xStartOffset - textLength60, y4 + descent, mLabelYPaint);


    }

    public static class PointItem {
        public String xLabel;
        public int yValue;

        public PointItem(String xLabel, int yValue) {
            this.xLabel = xLabel;
            this.yValue = yValue;
        }
    }
}
