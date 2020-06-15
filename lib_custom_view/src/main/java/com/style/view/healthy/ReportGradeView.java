package com.style.view.healthy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;


public class ReportGradeView extends View {
    private final String TAG = getClass().getSimpleName();
    //rate是一个（0, 0.5）区间内的值，数值越大，数值点之间的曲线弧度越小。
    private static final float SMOOTHNESS = 0.5f;

    private Path mShaderAreaPath;
    private Path mLinePath;

    private Paint linePaint;
    private Paint shaderAreaPaint;
    private TextPaint axisTextPaint;
    private TextPaint touchTextPaint;

    //曲线、竖线宽度
    private float mLineWidth;
    //颜色
    private int lineColor = 0xFF61A5E8;
    private int rectHighColor = 0xFFCDEAFF;
    private int rectLowColor = 0x47FCF6FC;
    private int whiteColor = 0xFFffffff;
    private int axisTextColor = 0xFFB3B3B3;
    private int touchTextColor = 0xFF242424;

    //控件宽高
    private int mViewWidth, mViewHeight;
    //边距
    private float paddingX, paddingY;
    //坐标文本宽高
    private float labelYWidth, labelHeight;
    //XY轴宽高
    private float mAxisWidth, mAxisHeight;
    //数据项间距
    private float mIntervalWidth;
    //点击文本
    private float touchLabelHeight, touchLabelWidth;
    private ArrayList<PointItem> dataList = new ArrayList<>();
    private ArrayList<String> mXlabelList = new ArrayList<>();
    private ArrayList<PointF> mControlPointList = new ArrayList<>();
    private Context context;
    private int mType;

    public ReportGradeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mLineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        paddingY = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        paddingX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        touchLabelWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        touchLabelHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        mShaderAreaPath = new Path();
        mLinePath = new Path();
        init(context);
        setData(1, getTestData());
    }

    private void init(Context context) {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeJoin(Paint.Join.ROUND);// 笔刷图形样式
        linePaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔转弯的连接风格
        linePaint.setDither(true);//防抖动
        linePaint.setShader(null);//设置渐变色
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(mLineWidth);

        shaderAreaPaint = new Paint(linePaint);
        shaderAreaPaint.setStyle(Paint.Style.FILL);

        axisTextPaint = new TextPaint();
        axisTextPaint.setAntiAlias(true);
        axisTextPaint.setTextAlign(Paint.Align.CENTER);
        axisTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        axisTextPaint.setColor(axisTextColor);
        labelHeight = axisTextPaint.getFontMetrics().bottom - axisTextPaint.getFontMetrics().top;
        labelYWidth = axisTextPaint.measureText("100");

        touchTextPaint = new TextPaint(axisTextPaint);
        touchTextPaint.setColor(touchTextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        mAxisWidth = mViewWidth - labelYWidth - 2 * paddingX;
        mAxisHeight = mViewHeight - labelHeight - 2 * paddingY;
        if (dataList.size() > 1) {
            mIntervalWidth = mAxisWidth / (dataList.size() - 1);
        }
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).x = i * mIntervalWidth;
            dataList.get(i).y = mAxisHeight * (100 - dataList.get(i).yValue) / 100;
        }
        calculateControlPoint(dataList);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e(TAG, "onLayout");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dataList.size() < 1) {
            return;
        }
        drawYLabel(canvas);
        drawXLabel(canvas);
        drawLineAndShaderArea(canvas);
        drawDataCircle(canvas);
    }

    private void drawYLabel(Canvas canvas) {
        canvas.save();
        canvas.translate((paddingX + labelYWidth) / 2, paddingY);
        canvas.drawText("100", 0, getBaseLine2CenterY(axisTextPaint.getFontMetrics()), axisTextPaint);
        canvas.drawText("50", 0, mAxisHeight / 2 + getBaseLine2CenterY(axisTextPaint.getFontMetrics()), axisTextPaint);
        canvas.drawText("0", 0, mAxisHeight + getBaseLine2CenterY(axisTextPaint.getFontMetrics()), axisTextPaint);
        canvas.restore();
    }

    private void drawXLabel(Canvas canvas) {
        canvas.save();
        canvas.translate(paddingX + labelYWidth, paddingY + mAxisHeight);
        float xOff = mAxisWidth / (mXlabelList.size() - 1);
        for (int i = 0; i < mXlabelList.size(); i++) {
            String xLabel = mXlabelList.get(i);
            float x = xOff * i;
            canvas.drawText(xLabel, x, (paddingY + labelHeight) / 2 + getBaseLine2CenterY(axisTextPaint.getFontMetrics()), axisTextPaint);
        }
        canvas.restore();
    }

    private void drawLineAndShaderArea(Canvas canvas) {
        if (dataList.size() <= 1) return;
        canvas.save();
        canvas.translate(paddingX + labelYWidth, paddingY);
        mShaderAreaPath.reset();
        mLinePath.reset();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(mLineWidth);
        for (int i = 0; i < dataList.size(); i++) {
            PointItem item = dataList.get(i);
            float x = item.x;
            float y = item.y;
            if (i == 0) {
                mShaderAreaPath.moveTo(x, y);
                mLinePath.moveTo(x, y);
            } else {
                PointF controlP1 = mControlPointList.get(i * 2 - 2);
                PointF controlP2 = mControlPointList.get(i * 2 - 1);
                mShaderAreaPath.cubicTo(controlP1.x, controlP1.y, controlP2.x, controlP2.y, x, y);
                mLinePath.cubicTo(controlP1.x, controlP1.y, controlP2.x, controlP2.y, x, y);
            }
        }
        mShaderAreaPath.lineTo(dataList.get(dataList.size() - 1).x, mAxisHeight);
        mShaderAreaPath.lineTo(dataList.get(0).x, mAxisHeight);
        mShaderAreaPath.lineTo(dataList.get(0).x, dataList.get(0).y);
        Shader shader = new LinearGradient(mAxisWidth / 2, 0, mAxisWidth / 2, mAxisHeight, rectHighColor, rectLowColor, Shader.TileMode.CLAMP);
        shaderAreaPaint.setShader(shader);
        canvas.drawPath(mShaderAreaPath, shaderAreaPaint);
        canvas.drawPath(mLinePath, linePaint);
        canvas.restore();
    }

    private void drawDataCircle(Canvas canvas) {
        if (mType == 1) {
            canvas.save();
            canvas.translate(paddingX + labelYWidth, paddingY);
            for (int i = 0; i < dataList.size(); i++) {
                PointItem item = dataList.get(i);
                float x = item.x;
                float y = item.y;
                linePaint.setStyle(Paint.Style.FILL);
                linePaint.setColor(whiteColor);
                canvas.drawCircle(x, y, mLineWidth * 7, linePaint);
                if (mSpot >= 0 && mSpot < dataList.size() && mSpot == i) {
                    linePaint.setColor(Color.parseColor("#00ff00"));
                } else
                    linePaint.setColor(lineColor);
                canvas.drawCircle(x, y, mLineWidth * 4, linePaint);
                linePaint.setStyle(Paint.Style.STROKE);
                linePaint.setStrokeWidth(mLineWidth);
                canvas.drawCircle(x, y, mLineWidth * 7, linePaint);

            }
            for (int i = 0; i < dataList.size(); i++) {
                if (mSpot >= 0 && mSpot < dataList.size() && mSpot == i) {
                    PointItem item = dataList.get(i);
                    float x = item.x;
                    float y = item.y;                    //画点中点数据
                    if (x < mAxisWidth + paddingX - touchLabelWidth - 50) {
                        touchTextPaint.setStyle(Paint.Style.STROKE);
                        canvas.drawRoundRect(x + 50, y - touchLabelHeight / 2, x + 50 + touchLabelWidth, y + touchLabelHeight / 2, 20, 20, touchTextPaint);
                        touchTextPaint.setStyle(Paint.Style.FILL);
                        canvas.drawText("05-06", x + 50 + touchLabelWidth / 2, y - touchLabelHeight / 2 + labelHeight, touchTextPaint);
                        canvas.drawText((int) item.yValue + "分", x + 50 + touchLabelWidth / 2, y + touchLabelHeight / 2 - labelHeight / 2, touchTextPaint);
                    } else {
                        touchTextPaint.setStyle(Paint.Style.STROKE);
                        canvas.drawRoundRect(x - 50 - touchLabelWidth, y - touchLabelHeight / 2, x - 50, y + touchLabelHeight / 2, 20, 20, touchTextPaint);
                        touchTextPaint.setStyle(Paint.Style.FILL);
                        canvas.drawText("05-06", x - 50 - touchLabelWidth / 2, y - touchLabelHeight / 2 + labelHeight, touchTextPaint);
                        canvas.drawText((int) item.yValue + "分", x - 50 - touchLabelWidth / 2, y + touchLabelHeight / 2 - labelHeight / 2, touchTextPaint);
                    }
                }
            }
            canvas.restore();
        }
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
        return Math.abs(fontMetrics.bottom - fontMetrics.top);
    }

    public void setData(int type, ArrayList<PointItem> data) {
        dataList.clear();
        dataList.addAll(data);
        this.mType = type;
        mSpot = -1;
        //requestLayout();
        //invalidate();
    }

    private ArrayList<PointItem> getTestData() {
        Random random = new Random();
        ArrayList<PointItem> list = new ArrayList<>();
        PointItem point;
        for (int i = 0; i < 7; i++) {
            point = new PointItem("周" + i, random.nextInt(80) + 20);
            list.add(point);
            mXlabelList.add(point.xLabel);
        }
        return list;
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
                PointItem nextPoint = pointList.get(1);
                float controlX = point.x + (nextPoint.x - point.x) * SMOOTHNESS;
                float controlY = point.y;
                mControlPointList.add(new PointF(controlX, controlY));
            } else if (i == pointList.size() - 1) {//最后一项
                //添加前控制点
                PointItem lastPoint = pointList.get(i - 1);
                float controlX = point.x - (point.x - lastPoint.x) * SMOOTHNESS;
//                float controlX = point.x - (point.x - lastPoint.x) * SMOOTHNESS;
                float controlY = point.y;
                mControlPointList.add(new PointF(controlX, controlY));
            } else {//中间项
                PointItem lastPoint = pointList.get(i - 1);
                PointItem nextPoint = pointList.get(i + 1);
//                float k = (nextPoint.y - lastPoint.y) / (nextPoint.x - lastPoint.x);
//                float b = point.y - k * point.x;
                //添加前控制点
                float lastControlX = point.x - (point.x - lastPoint.x) * SMOOTHNESS;
//                float lastControlX = point.x - (point.x - lastPoint.x) * SMOOTHNESS;
                float lastControlY = point.y;
//                float lastControlY = k * lastControlX + b;
                mControlPointList.add(new PointF(lastControlX, lastControlY));
                //添加后控制点
                float nextControlX = point.x + (nextPoint.x - point.x) * SMOOTHNESS;
                float nextControlY = point.y;
//                float nextControlY = k * nextControlX + b;
                mControlPointList.add(new PointF(nextControlX, nextControlY));
//                mControlPointList.add(new PointF(nextControlX, nextControlY));
            }
        }
    }

    private int mSpot = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (dataList.size() > 0 && isValidTouch(event.getX(), event.getY())) {
                    mSpot = getTouchSpot(event.getX());
                    invalidate();
                    return true;
                } else {
                    mSpot = -1;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }

    //根据点击的范围获取到当前点击的点
    private int getTouchSpot(float x) {
        int index = 0;
        for (int i = 0; i < dataList.size(); i++) {
            float axisX = dataList.get(i).x;
            if (x > paddingX + labelYWidth + axisX - mIntervalWidth / 2 && x < paddingX + labelYWidth + axisX + mIntervalWidth / 2) {
                index = i;
            }
        }
        return index;
    }

    //判断当前点击的范围是否需要处理
    private boolean isValidTouch(float x, float y) {
        boolean ignore = false;
        if ((x >= (paddingX + labelYWidth - mIntervalWidth / 2) && x <= (paddingX + labelYWidth + dataList.get(dataList.size() - 1).x + mIntervalWidth / 2)) && (y >= 0 && y <= paddingY + mAxisHeight)) {
            ignore = true;
        }
        return ignore;
    }

    public static class PointItem {
        public String xLabel;
        public float yValue;
        public float x;
        public float y;

        public PointItem(String xLabel, float yValue, float x, float y) {
            this.xLabel = xLabel;
            this.yValue = yValue;
            this.x = x;
            this.y = y;
        }

        public PointItem(String xLabel, float yValue) {
            this.xLabel = xLabel;
            this.yValue = yValue;
        }

        public PointItem() {

        }

        @Override
        public String toString() {
            return "PointItem{" +
                    "xLabel='" + xLabel + '\'' +
                    ", yValue=" + yValue +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
