package com.style.view.healthy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiajun on 2017/7/21.
 */

public class TemperatureLineNew extends View {
    //rate是一个（0, 0.5）区间内的值，数值越大，数值点之间的曲线弧度越小。
    private static final float SMOOTHNESS = 0.4f;
    private final String TAG = this.getClass().getSimpleName();
    private int mWidth;
    private int mHeight;
    //曲线画笔
    private Paint mLinePaint;
    private Path mLineNoDataPath;
    private int colorChart = 0xFF45CE7B;
    private int colorLabelY = 0xffaaaaaa;
    private int colorGrid = 0x99CCCCCC;
    //曲线宽度
    private int lineWidth = 1;
    //顶点标签文本
    private TextPaint mLabelValuePaint;
    private TextPaint mLabelXPaint;
    private Path mLinePath;

    private static final float DEFAULT_MIN = 35.0F;
    private static final float DEFAULT_MAX = 38.0F;
    private float yMaxValue = DEFAULT_MAX;
    private float yMinValue = DEFAULT_MIN;
    private int defaultWidth;
    //X轴坐标间隔宽度
    private int xIntervalWidth = 60;
    //X轴起始偏移宽度
    private int xStartOffset = 20;
    private int xEndOffset = 20;
    //Y轴顶端距离曲线布局高度
    private float yStartOffset = 20f;
    private float yEndOffset = 40f;

    private ArrayList<PointItem> dataList;
    private Paint mNoDataPathPaint;
    private float yValueAllHeight;
    private ArrayList<PointF> mControlPointList = new ArrayList<>();

    public TemperatureLineNew(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        defaultWidth = dp2px(getContext(), 300);
        mWidth = defaultWidth;
        xIntervalWidth = dp2px(getContext(), xIntervalWidth);
        xStartOffset = dp2px(getContext(), xStartOffset);
        xEndOffset = dp2px(getContext(), xEndOffset);
        yStartOffset = dp2px(getContext(), (int) yStartOffset);
        yEndOffset = dp2px(getContext(), (int) yEndOffset);
        lineWidth = dp2px(getContext(), lineWidth);
        initPaint();
        dataList = new ArrayList<>();
        mLinePath = new Path();
        mLineNoDataPath = new Path();

    }

    private void initPaint() {
        mLabelXPaint = new TextPaint();
        mLabelXPaint.setAntiAlias(true);
        mLabelXPaint.setColor(colorLabelY);
        mLabelXPaint.setTextAlign(Paint.Align.CENTER);
        mLabelXPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));

        mLabelValuePaint = new TextPaint();
        mLabelValuePaint.setAntiAlias(true);
        mLabelValuePaint.setColor(colorLabelY);
        mLabelValuePaint.setTextAlign(Paint.Align.CENTER);
        mLabelValuePaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);// 笔刷图形样式
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔转弯的连接风格
        mLinePaint.setDither(true);//防抖动
        mLinePaint.setShader(null);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(colorChart);
        mLinePaint.setStrokeWidth(lineWidth);

        mNoDataPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNoDataPathPaint.setDither(true);//防抖动
        mNoDataPathPaint.setShader(null);
        mNoDataPathPaint.setStyle(Paint.Style.STROKE);
        mNoDataPathPaint.setColor(colorChart);
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
        yValueAllHeight = mHeight - yStartOffset - yEndOffset;
        Log.e(TAG, "onMeasure--" + mWidth + "  " + mHeight);
        for (int i = 0; i < dataList.size(); i++) {
            float x = xIntervalWidth * i;
            float y = - yValueAllHeight * (dataList.get(i).yValue - yMinValue) / (yMaxValue - yMinValue);
            dataList.get(i).x = x;
            dataList.get(i).y = y;
        }
        calculateControlPoint(dataList);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mLinePath.reset();
        //mLineNoDataPath.reset();
        if (dataList == null || dataList.isEmpty()) {
            //canvas.drawText("没有历史数据哦", mWidth / 2, mHeight / 2, mLabelXPaint);
            return;
        }

        float textLength = mLabelXPaint.measureText("00:00");
        float descent = mLabelXPaint.descent();
        float textHeight = mLabelXPaint.getFontMetrics().bottom - mLabelXPaint.getFontMetrics().top;

        canvas.translate(xStartOffset, yStartOffset + yValueAllHeight);
        for (int i = 0; i < dataList.size(); i++) {
            PointItem item = dataList.get(i);
            String xLabel = item.xLabel;
            float x = item.x;
            float y = item.y;
            canvas.drawText(xLabel, x, textHeight, mLabelXPaint);
            //只有一条数据时
            if (dataList.size() == 1) {
                canvas.drawLine(x, 0, x, y, mLinePaint);
                return;
            }

            if (i == 0) {
                mLinePath.moveTo(x, y);
            } else {
                PointF controlP1 = mControlPointList.get(i * 2 - 2);
                PointF controlP2 = mControlPointList.get(i * 2 - 1);
                mLinePath.cubicTo(controlP1.x, controlP1.y, controlP2.x, controlP2.y, x, y);
            }
        }
        mLinePath.lineTo(dataList.get(dataList.size() - 1).x, 0);
        mLinePath.lineTo(dataList.get(0).x, 0);
        mLinePath.lineTo(dataList.get(0).x, dataList.get(0).y);
        canvas.drawPath(mLinePath, mLinePaint);
        canvas.restore();
    }

    public static class PointItem {
        public String xLabel;
        public float yValue;
        public float x;
        public float y;

        public PointItem(String xLabel, float yValue) {
            this.xLabel = xLabel;
            this.yValue = yValue;
        }

        public PointItem() {

        }
    }

    /**
     * 计算每个点的三阶贝塞尔曲线控制点，第一个点不需要控制点
     *
     * @return
     */
    private void calculateControlPoint(ArrayList<PointItem> pointList) {
        mControlPointList.clear();
        if (pointList.size() <= 1) {
            return;
        }
        for (int i = 0; i < pointList.size(); i++) {
            PointItem point = pointList.get(i);
            if (i == 0) {//第一项
                //添加后控制点
                PointItem nextPoint = pointList.get(i + 1);
                float controlX = point.x + (nextPoint.x - point.x) * SMOOTHNESS;
                float controlY = point.y;
                mControlPointList.add(new PointF(controlX, controlY));
            } else if (i == pointList.size() - 1) {//最后一项
                //添加前控制点
                PointItem lastPoint = pointList.get(i - 1);
                float controlX = point.x - (point.x - lastPoint.x) * SMOOTHNESS;
                float controlY = point.y;
                mControlPointList.add(new PointF(controlX, controlY));
            } else {//中间项
                PointItem lastPoint = pointList.get(i - 1);
                PointItem nextPoint = pointList.get(i + 1);
                float k = (nextPoint.y - lastPoint.y) / (nextPoint.x - lastPoint.x);
                float b = point.y - k * point.x;
                //添加前控制点
                float lastControlX = point.x - (point.x - lastPoint.x) * SMOOTHNESS;
                float lastControlY = k * lastControlX + b;
                mControlPointList.add(new PointF(lastControlX, lastControlY));
                //添加后控制点
                float nextControlX = point.x + (nextPoint.x - point.x) * SMOOTHNESS;
                float nextControlY = k * nextControlX + b;
                mControlPointList.add(new PointF(nextControlX, nextControlY));
            }
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
