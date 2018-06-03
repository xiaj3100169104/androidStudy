package fussen.cc.barchart.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.style.framework.ChartAnimator;

/**
 * Created by Fussen on 2017/4/21.
 * <p>
 * 不可滚动的条形图
 */
public class BarChart extends View {

    private static final String TAG = "BarChart";
    private Paint mChartPaint;
    private Rect mBound;

    private float textStart;
    private int mHeight;
    private int mWidth;

    private List<Float> verticalList = new ArrayList<>();
    private List<String> horizontalList = new ArrayList<>();

    private float verticalWidth = 100f;
    private float chartWidth; //表的总宽度，除过外间距
    private float outSpace = verticalWidth;// 柱子与纵轴的距离
    private float startChart = verticalWidth; //柱子开始的横坐标
    private float interval;//柱子之间的间隔

    private float bottomHeight = 100f;//底部横坐标高度
    private float barWidth;//柱子的真实宽度
    private float preBarWidth;

    private String maxValue = "2";//默认最大值
    private String middleValue = "1";

    private int paddingTop = 20;

    private int chartHeight = 10;//默认柱子高度
    private Paint noDataPaint;
    private Paint textXpaint;
    private Paint linePaint;

    private String noDataColor = "#66FF6933";
    private String textColor = "#FFBEBEBE";
    private String lineColor = "#E4E5E6";
    private String chartColor = "#FF6933";
    private int mDuriation = 3000;
    private boolean isShort = false;
    private float allInteval;
    private float allBarwidth;
    private float barPercent = 0.1f;
    private float intevalPercent = 0.04f;//间隔占总表格的比例 100%是1

    private float allChartWidth;//表格的柱子和间隔的总宽
    private Paint textYpaint;


    private ChartAnimator mAnimator;


    public BarChart(Context context) {
        this(context, null);
    }

    public BarChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //宽度的模式
        int mWidthModle = MeasureSpec.getMode(widthMeasureSpec);
        //宽度大小
        int mWidthSize = MeasureSpec.getSize(widthMeasureSpec);

        int mHeightModle = MeasureSpec.getMode(heightMeasureSpec);
        int mHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        //如果明确大小,直接设置大小
        if (mWidthModle == MeasureSpec.EXACTLY) {
            mWidth = mWidthSize;
        } else {
            //计算宽度,可以根据实际情况进行计算
            mWidth = (getPaddingLeft() + getPaddingRight());
            //如果为AT_MOST, 不允许超过默认宽度的大小
            if (mWidthModle == MeasureSpec.AT_MOST) {
                mWidth = Math.min(mWidth, mWidthSize);
            }
        }
        if (mHeightModle == MeasureSpec.EXACTLY) {
            mHeight = mHeightSize;
        } else {
            mHeight = (getPaddingTop() + getPaddingBottom());
            if (mHeightModle == MeasureSpec.AT_MOST) {
                mHeight = Math.min(mHeight, mHeightSize);
            }
        }
        //设置测量完成的宽高
        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight() - paddingTop;

        chartWidth = mWidth - outSpace;
        if (isShort) {
            //每个柱子宽度
            barWidth = (int) (chartWidth * barPercent);
            interval = (int) (chartWidth * intevalPercent);


            //所有柱子宽度 之和
            allBarwidth = horizontalList.size() * barWidth;
            //所有间隔宽 之和
            allInteval = (horizontalList.size() - 1) * interval;

            //所有柱子和间隔的宽度
            allChartWidth = allBarwidth + allInteval;

            //柱子开始的横坐标

            startChart = outSpace + (chartWidth / 2f - allChartWidth / 2f);
        } else {
            //每个柱子的宽度 没有间隔

            try {
                preBarWidth = chartWidth / verticalList.size();
            } catch (Exception e) {
            }

            //柱子之间的间隔 为没有间隔柱子的宽度的30%
            interval = preBarWidth / 10f * 3f;
            barWidth = preBarWidth - interval;

            startChart = verticalWidth;
        }

        //////////////////////////////////////////

        //横坐标

        textStart = startChart + (barWidth / 2f);

    }

    private void init() {

        //初始化动画
        mAnimator = new ChartAnimator(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
            }
        });

        mBound = new Rect();


        //柱子画笔
        mChartPaint = new Paint();
        mChartPaint.setAntiAlias(true);
        mChartPaint.setColor(Color.parseColor(chartColor));


        //线画笔
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor(lineColor));

        //x纵坐标 画笔
        textXpaint = new Paint();
        textXpaint.setAntiAlias(true);
        textXpaint.setTextSize(27f);
        textXpaint.setTextAlign(Paint.Align.CENTER);
        textXpaint.setColor(Color.parseColor(textColor));

        //Y纵坐标 画笔
        textYpaint = new Paint();
        textYpaint.setAntiAlias(true);
        textYpaint.setTextSize(28f);
        textYpaint.setTextAlign(Paint.Align.LEFT);
        textYpaint.setColor(Color.parseColor(textColor));

        //无数据时的画笔
        noDataPaint = new Paint();
        noDataPaint.setAntiAlias(true);
        noDataPaint.setColor(Color.parseColor(noDataColor));
        noDataPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float lineInterval = (mHeight - bottomHeight) / 4f; //横线之间的间距  纵向
        float textHeight = mHeight + paddingTop - bottomHeight;//横坐标高度

        //画线
        drawLine(canvas, lineInterval, textHeight);

        //画纵坐标
        drawYtext(canvas, lineInterval, textHeight);

        //画横坐标
        float textTempStart = textStart;

        drawXtext(canvas, textTempStart);


        float chartTempStart = startChart;

        float size = (mHeight - bottomHeight) / 100f; //比例

        //画柱子
        drawBar(canvas, chartTempStart, size);

    }

    /**
     * 画柱子
     *
     * @param canvas
     * @param chartTempStart
     * @param size
     */
    private void drawBar(Canvas canvas, float chartTempStart, float size) {
        if (isShort) {

            for (int i = 0; i < verticalList.size(); i++) {

                //每个数据点所占的Y轴高度
                float barHeight = verticalList.get(i) / Float.valueOf(maxValue) * 100f * size;

                float realBarHeight = barHeight * mAnimator.getPhaseY();

                if (verticalList.get(0) == 0) {
                    canvas.drawRect(chartTempStart,
                            mHeight - bottomHeight + paddingTop - chartHeight
                            , chartTempStart + barWidth,
                            mHeight + paddingTop - bottomHeight
                            , noDataPaint);
                } else {
                    canvas.drawRect(chartTempStart,
                            (mHeight - bottomHeight + paddingTop) - realBarHeight
                            , chartTempStart + barWidth,
                            mHeight + paddingTop - bottomHeight
                            , mChartPaint);
                }

                chartTempStart += (barWidth + interval);

            }

        } else {
            for (int i = 0; i < verticalList.size(); i++) {

                //每个数据点所占的Y轴高度
                float barHeight = verticalList.get(i) / Float.valueOf(maxValue) * 100f * size;

                float realBarHeight = barHeight * mAnimator.getPhaseY();

                //画柱状图 矩形
                RectF rectF = new RectF();
                rectF.left = chartTempStart;
                rectF.right = chartTempStart + barWidth;
                rectF.bottom = mHeight + paddingTop - bottomHeight;

                if (verticalList.get(i) == 0) {//如果纵轴为0情况处理
                    rectF.top = mHeight - bottomHeight + paddingTop - chartHeight;
                    canvas.drawRect(rectF, noDataPaint);
                } else {
                    rectF.top = (mHeight - bottomHeight + paddingTop) - realBarHeight;
                    canvas.drawRect(rectF, mChartPaint);
                }
                chartTempStart += preBarWidth;

            }
        }
    }

    /**
     * 画x轴
     *
     * @param canvas
     * @param textTempStart
     */
    private void drawXtext(Canvas canvas, float textTempStart) {
        if (isShort) {

            for (int i = 0; i < verticalList.size(); i++) {
                textXpaint.getTextBounds(horizontalList.get(i), 0, horizontalList.get(i).length(), mBound);

                Paint.FontMetricsInt fontMetrics = textXpaint.getFontMetricsInt();
                int baseline = (int) (mHeight + paddingTop - bottomHeight + ((bottomHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top));
                canvas.drawText(horizontalList.get(i), textTempStart, baseline, textXpaint);
//                canvas.drawLine(textTempStart, mHeight + paddingTop - bottomHeight, textTempStart, 0, linePaint);
                textTempStart += (barWidth + interval);
            }

        } else {
            if (horizontalList.size() < 13) {
                for (int i = 0; i < horizontalList.size(); i++) {

//                canvas.drawLine(textTempStart, mHeight + paddingTop - bottomHeight, textTempStart, 0, linePaint);


                    //画横轴
                    textXpaint.getTextBounds(horizontalList.get(i), 0, horizontalList.get(i).length(), mBound);

                    Paint.FontMetricsInt fontMetrics = textXpaint.getFontMetricsInt();

                    int baseline = (int) (mHeight + paddingTop - bottomHeight + ((bottomHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top));
                    canvas.drawText(horizontalList.get(i), textTempStart, baseline, textXpaint);

                    textTempStart += preBarWidth;
                }
            } else {
                for (int i = 0; i < horizontalList.size(); i++) {

                    if (i % 4 == 0) {
                        //画横轴
                        textXpaint.getTextBounds(horizontalList.get(i), 0, horizontalList.get(i).length(), mBound);

                        Paint.FontMetricsInt fontMetrics = textXpaint.getFontMetricsInt();
                        int baseline = (int) (mHeight + paddingTop - bottomHeight + ((bottomHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top));
                        canvas.drawText(horizontalList.get(i), textTempStart, baseline, textXpaint);
                    }
                    textTempStart += preBarWidth;
                }
            }
        }
    }

    /**
     * 画Y轴
     *
     * @param canvas
     * @param lineInterval
     * @param textHeight
     */
    private void drawYtext(Canvas canvas, float lineInterval, float textHeight) {
        canvas.drawText("0", 0f, textHeight, textYpaint);

        canvas.drawText(middleValue, 0f, textHeight - lineInterval * 2f + 10f, textYpaint);
        canvas.drawText(maxValue, 0f, textHeight - lineInterval * 4f + 10f, textYpaint);
    }

    /**
     * 画线
     *
     * @param canvas
     * @param lineInterval
     * @param textHeight
     */
    private void drawLine(Canvas canvas, float lineInterval, float textHeight) {
        canvas.drawLine(outSpace - 10f, textHeight, mWidth, textHeight, linePaint);
        canvas.drawLine(outSpace - 10f, textHeight - lineInterval, mWidth, textHeight - lineInterval, linePaint);
        canvas.drawLine(outSpace - 10f, textHeight - lineInterval * 2f, mWidth, textHeight - lineInterval * 2f, linePaint);
        canvas.drawLine(outSpace - 10f, textHeight - lineInterval * 3f, mWidth, textHeight - lineInterval * 3f, linePaint);
        canvas.drawLine(outSpace - 10f, textHeight - lineInterval * 4f, mWidth, textHeight - lineInterval * 4f, linePaint);
    }


    /**
     * 重新指定起始位置
     *
     * @param verticalList
     */
    private void measureWidthShort(List<Float> verticalList) {
        isShort = true;
        //每个柱子宽度

        barWidth = (int) (chartWidth * barPercent);
        interval = (int) (chartWidth * intevalPercent);


        //所有柱子宽度 之和
        allBarwidth = verticalList.size() * barWidth;
        //所有间隔宽 之和
        allInteval = (verticalList.size() - 1) * interval;

        //所有柱子和间隔的宽度
        allChartWidth = allBarwidth + allInteval;

        //柱子开始的横坐标

        startChart = outSpace + (chartWidth / 2f - allChartWidth / 2f);

        textStart = startChart + barWidth / 2f;
    }

    /**
     * 重新指定起始位置
     *
     * @param verticalList
     */
    private void measureWidth(List<Float> verticalList) {
        isShort = false;
        //每个柱子的宽度 没有间隔
        try {
            preBarWidth = chartWidth / verticalList.size();
        } catch (Exception e) {
        }
        //柱子之间的间隔 为没有间隔柱子的宽度的30%
        interval = preBarWidth / 10f * 3f;
        barWidth = preBarWidth - interval;
        startChart = verticalWidth;
        textStart = startChart + (barWidth / 2f);
    }


    /**
     * 设置纵轴数据
     *
     * @param verticalList
     */
    public void setVerticalList(List<Float> verticalList) {


        if (verticalList != null) {
            this.verticalList = verticalList;

        } else {
            maxValue = "2";
            middleValue = "1";
            invalidate();
            return;
        }

        if (verticalList.size() <= 6) {

            measureWidthShort(verticalList);

            if (Collections.max(verticalList) > 2) {
                int tempMax = Math.round(Collections.max(verticalList));
                while (tempMax % 10 != 0) {
                    tempMax++;
                }
                int middle = tempMax / 2;
                maxValue = String.valueOf(tempMax);
                middleValue = String.valueOf(middle);
            } else {
                maxValue = "2";
                middleValue = "1";
            }
        } else {

            measureWidth(verticalList);

            if (Collections.max(verticalList) > 2) {
                int tempMax = Math.round(Collections.max(verticalList));
                while (tempMax % 10 != 0) {
                    tempMax++;
                }
                int middle = tempMax / 2;
                maxValue = String.valueOf(tempMax);
                middleValue = String.valueOf(middle);
            } else {
                maxValue = "2";
                middleValue = "1";
            }

        }

        mAnimator.animateY(mDuriation);
    }

    /**
     * 设置横轴数据
     *
     * @param horizontalList
     */
    public void setHorizontalList(List<String> horizontalList) {
        if (horizontalList != null)
            this.horizontalList = horizontalList;
    }

}