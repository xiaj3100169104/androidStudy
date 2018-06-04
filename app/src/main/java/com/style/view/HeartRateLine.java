package com.style.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.style.view.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiajun on 2017/7/21.
 */

public class HeartRateLine extends View {
    private final String TAG = this.getClass().getSimpleName();
    private int mWidth;
    private int mHeight;
    //曲线画笔
    private Paint mLinePaint;
    private Path mLineNoDataPath;

    //曲线颜色
    private int lineColor = 0xFF91C532;
    //曲线宽度
    private int lineWidth = 2;
    //顶点标签文本
    private TextPaint mLabelValuePaint;
    private TextPaint mLabelXPaint;
    private Path mLinePath;

    private float yMaxValue = 150f;
    private float yMinValue = 0f;

    //X轴坐标间隔宽度
    private int xIntervalWidth = 20;
    //X轴起始偏移宽度
    private int xStartOffset = 20;
    private int xEndOffset = 20;

    //Y轴顶端距离曲线布局高度
    private float yStartOffset = 40f;
    private float yEndOffset = 40f;

    private ArrayList<PointItem> dataList;
    private int defaultWidth;
    private Paint mNoDataPathPaint;
    //内容区偏移量，
    private int mContentOffset;
    public HeartRateLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        defaultWidth = Utils.dp2px(getContext(), 300);
        mWidth = defaultWidth;
        xIntervalWidth = Utils.dp2px(getContext(), xIntervalWidth);
        xStartOffset = Utils.dp2px(getContext(), xStartOffset);
        xEndOffset = Utils.dp2px(getContext(), xEndOffset);

        yEndOffset = Utils.dp2px(getContext(), (int) yEndOffset);
        yStartOffset = Utils.dp2px(getContext(), (int) yStartOffset);
        lineWidth = Utils.dp2px(getContext(), lineWidth);
        initPaint();
        dataList = new ArrayList<>();
        mLinePath = new Path();
        mLineNoDataPath = new Path();

    }

    private void initPaint() {
        mLabelXPaint = new TextPaint();
        mLabelXPaint.setAntiAlias(true);
        mLabelXPaint.setColor(0xffaaaaaa);
        mLabelXPaint.setTextAlign(Paint.Align.CENTER);
        mLabelXPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));

        mLabelValuePaint = new TextPaint();
        mLabelValuePaint.setAntiAlias(true);
        mLabelValuePaint.setColor(0xffaaaaaa);
        mLabelValuePaint.setTextAlign(Paint.Align.CENTER);
        mLabelValuePaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);// 笔刷图形样式
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔转弯的连接风格
        mLinePaint.setDither(true);//防抖动
        mLinePaint.setShader(null);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(lineColor);
        mLinePaint.setStrokeWidth(lineWidth);

        mNoDataPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNoDataPathPaint.setDither(true);//防抖动
        mNoDataPathPaint.setShader(null);
        mNoDataPathPaint.setStyle(Paint.Style.STROKE);
        mNoDataPathPaint.setColor(lineColor);
        mNoDataPathPaint.setStrokeWidth(lineWidth);
        DashPathEffect pathEffect = new DashPathEffect(new float[]{15, 15}, 0);
        mNoDataPathPaint.setPathEffect(pathEffect);
    }

    public void setData(List<PointItem> list) {
        dataList.clear();
        mWidth = defaultWidth;
        if (list != null && !list.isEmpty()) {
            dataList.addAll(list);
            mWidth = xIntervalWidth * (dataList.size() - 1) + xStartOffset + xEndOffset;
        }
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG, "onMeasure--" + mWidth + "  " + mHeight);
        setMeasuredDimension(mWidth, mHeight);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e(TAG, "onLayout-->  " + "changed:" + changed + " left:" + left + " top:" + top + "  right:" + right + "  bottom:" + bottom);

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e(TAG, "onScrollChanged-->  " + " left:" + l + " top:" + t + "  oldl:" + oldl + "  oldt:" + oldt);
        mContentOffset = l;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        mLinePath.reset();
        mLineNoDataPath.reset();
        if (dataList == null || dataList.isEmpty()) {
            canvas.drawText("没有历史数据哦", mWidth / 2, mHeight / 2, mLabelXPaint);
            return;
        }
        float yValueAllHeight = mHeight - yStartOffset - yEndOffset;

        float textLength = mLabelXPaint.measureText("00:00");
        float descent = mLabelXPaint.descent();
        float textHeight = mLabelXPaint.getFontMetrics().bottom - mLabelXPaint.getFontMetrics().top;

        boolean isNoDataStart = false;
        float xNoDataStart = 0;
        float yNoDataStart = 0;
        float xNoDataEnd;
        float yNoDataEnd;
        for (int i = 0; i < dataList.size(); i++) {
            PointItem item = dataList.get(i);
            String xLabel = item.xLabel;
            float yValue = item.yValue;
            float x = xIntervalWidth * i + xStartOffset;
            float y = yValueAllHeight - yValueAllHeight * (yValue - yMinValue) / (yMaxValue - yMinValue) + yStartOffset;
            //Log.e(TAG, "x=" + x + "   y=" + y);
            //画x轴标签,间隔5分钟
            if (i % 5 == 0) {
                Log.e(TAG, "i--" + i + "  x--" + x);
                canvas.drawText(xLabel, x, mHeight - yStartOffset + textHeight, mLabelXPaint);
            }
            //画点
            if (yValue > 0)
                canvas.drawCircle(x, y, mLinePaint.getStrokeWidth() / 2, mLinePaint);
            //画异常值顶点标签
            if (yValue < yMaxValue && yValue > 100) {
                canvas.drawText(String.valueOf((int) yValue), x, y - descent * 2, mLabelValuePaint);
            }
            if (yValue > yMinValue && yValue < 60) {
                canvas.drawText(String.valueOf((int) yValue), x, y + textHeight, mLabelValuePaint);
            }

            if (yValue > 0 && isNoDataStart) {
                //mLineNoDataPath.lineTo(x, y);
                xNoDataEnd = x;
                yNoDataEnd = y;
                canvas.drawLine(xNoDataStart, yNoDataStart, xNoDataEnd, yNoDataEnd, mNoDataPathPaint);
                isNoDataStart = false;
            }

            if (yValue > 0) {
                if ((i + 1) <= dataList.size() - 1) {
                    PointItem nextItem = dataList.get(i + 1);
                    float yValue2 = nextItem.yValue;
                    if (yValue2 > 0) {
                        float x2 = xIntervalWidth * (i + 1) + xStartOffset;
                        float y2 = yValueAllHeight - yValueAllHeight * (yValue2 - yMinValue) / (yMaxValue - yMinValue) + yStartOffset;
                        canvas.drawLine(x, y, x2, y2, mLinePaint);

                    } else {
                        if (!isNoDataStart) {
                            xNoDataStart = x;
                            yNoDataStart = y;
                            //mLineNoDataPath.moveTo(x, y);
                            isNoDataStart = true;
                        }
                    }
                }
            }

        }
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
