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

import com.style.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiajun on 2017/7/21.
 */

public class TemperatureLineNew extends View {
    private final String TAG = this.getClass().getSimpleName();
    private int mWidth;
    private int mHeight;
    //曲线画笔
    private Paint mLinePaint;
    private Path mLineNoDataPath;

    //曲线颜色
    private int lineColor = 0xFFFFFFFF;
    //曲线宽度
    private int lineWidth = 2;
    //顶点标签文本
    private TextPaint mLabelValuePaint;
    private TextPaint mLabelXPaint;
    private Path mLinePath;

    private float yMaxValue = 38.0f;
    private float yMinValue = 35.0f;

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

    public TemperatureLineNew(Context context, @Nullable AttributeSet attrs) {
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
        mLabelXPaint.setColor(lineColor);
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

    public void setMaxAndMin(float max, float min) {
        this.yMaxValue = (float) (Math.ceil(max) < yMaxValue ? yMaxValue : Math.ceil(max));
        this.yMinValue = (float) (Math.floor(min) > yMinValue ? yMinValue : Math.floor(min));
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
    protected void onDraw(Canvas canvas) {
        mLinePath.reset();
        mLineNoDataPath.reset();
        if (dataList == null || dataList.isEmpty()) {
            canvas.drawText("没有历史数据哦", mWidth / 2, mHeight / 2, mLabelXPaint);
            return;
        }

        float textLength = mLabelXPaint.measureText("00:00");
        float descent = mLabelXPaint.descent();
        float textHeight = mLabelXPaint.getFontMetrics().bottom - mLabelXPaint.getFontMetrics().top;

        ArrayList<Integer> hasDataIndex = new ArrayList<>();
        ArrayList<Float> indexData = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            PointItem item = dataList.get(i);
            String xLabel = item.xLabel;
            float yValue = item.yValue;
            float x = xIntervalWidth * i + xStartOffset;
            //画x轴标签,间隔5分钟
            if (i % 5 == 0) {
                canvas.drawText(xLabel, x, mHeight - yStartOffset + textHeight, mLabelXPaint);
            }
            if (dataList.get(i).yValue > 0) {
                hasDataIndex.add(i);
                indexData.add(dataList.get(i).yValue);
            }
        }

        float yValueAllHeight = mHeight - yStartOffset - yEndOffset;

        for (int i = 0; i < hasDataIndex.size(); i++) {
            int index = hasDataIndex.get(i);
            float x = xIntervalWidth * (index) + xStartOffset;
            float y = yValueAllHeight - yValueAllHeight * (dataList.get(index).yValue - yMinValue) / (yMaxValue - yMinValue) + yStartOffset;

            canvas.drawCircle(x, y, mLinePaint.getStrokeWidth() / 2, mLinePaint);

            //Log.e(TAG, "x=" + x + "   y=" + y);
            if ((i + 1) <= hasDataIndex.size() - 1) {
                int nextIndex = hasDataIndex.get(i + 1);
                float x2 = xIntervalWidth * (nextIndex) + xStartOffset;
                float y2 = yValueAllHeight - yValueAllHeight * (dataList.get(nextIndex).yValue - yMinValue) / (yMaxValue - yMinValue) + yStartOffset;
                if (nextIndex - index <= 3) {
                    canvas.drawLine(x, y, x2, y2, mLinePaint);
                } else {
                    canvas.drawLine(x, y, x2, y2, mNoDataPathPaint);
                }
            }
        }
    }

    public static class PointItem {
        public String xLabel;
        public float yValue;

        public PointItem(String xLabel, float yValue) {
            this.xLabel = xLabel;
            this.yValue = yValue;
        }
    }
}
