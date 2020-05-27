package com.style.view.other;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.style.lib_custom_view.R;
import com.style.view.progressbar.BaseProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 饼状图
 */
public class PieChartView extends BaseProgressBar {

    private final float default_stroke_width;
    private final float default_src_offset = 0f;
    private Paint paint;
    protected Paint textPaint;
    private RectF rectF = new RectF();

    private float strokeWidth;
    private float srcOffset;
    private int interval;
    private float arcFinishedStartAngle = 0;
    private List<PartItem> items;
    private int mViewWidth;
    private int mViewHeight;

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        default_stroke_width = dp2px(context, 4);
        interval = dp2px(context, 1);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PieChart, defStyleAttr, 0);
        strokeWidth = attributes.getDimension(R.styleable.PieChart_pie_chart_stroke_width, default_stroke_width);
        arcFinishedStartAngle = attributes.getFloat(R.styleable.PieChart_pie_chart_finished_start_angle, arcFinishedStartAngle);
        srcOffset = attributes.getDimension(R.styleable.PieChart_pie_chart_arc_offset, default_src_offset);
        attributes.recycle();
        initPainters();
        setItems(getTestData());
    }

    protected void initPainters() {
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        //paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<PartItem> data = getItems();
        if (data == null || data.size() <= 0)
            return;
        canvas.save();
        //以画布中心点旋转坐标系
        canvas.rotate(arcFinishedStartAngle, getWidth() / 2, getHeight() / 2);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        int startAngle = 0;
        int totalAngle = 0;
        for (int i = 0; i < data.size(); i++) {
            PartItem item = data.get(i);
            paint.setColor(item.color);
            float itemSweepAngle = item.progress / (float) getMax() * 360;
            float progressAngle = getProgress() / (float) getMax() * 360;

            //饼状图矩形区域,注意这里的矩形边界是指矩形边框的中间位置
            rectF.set(strokeWidth / 1f, strokeWidth / 1f, mViewWidth - strokeWidth / 1f, mViewHeight - strokeWidth / 1f);
            float a;
            if (i == 0)
                a = itemSweepAngle / 2;
            else
                a = totalAngle + itemSweepAngle / 2;
            totalAngle += itemSweepAngle;

            float xOffset = 0.0f, yOffset = 0.0f;
            if (srcOffset > 0) {
                xOffset = (float) (srcOffset * Math.cos(Math.toRadians(a)));
                yOffset = (float) (srcOffset * Math.sin(Math.toRadians(a)));
            }
            rectF.offset(xOffset, yOffset);
            //有动画时
            if (startAngle + itemSweepAngle > progressAngle) {
                itemSweepAngle = progressAngle - startAngle;
                canvas.drawArc(rectF, startAngle, itemSweepAngle, false, paint);
                break;
            }
            canvas.drawArc(rectF, startAngle, itemSweepAngle, false, paint);
            startAngle += itemSweepAngle;
        }
        canvas.restore();
    }

    public void setItems(List<PartItem> items) {
        this.items = items;
        setProgress(getMax());
        invalidate();
    }

    public void setItemsWithAnimation(List<PartItem> items) {
        this.items = items;
        setProgressWithAnimation(getMax());
    }

    public List<PartItem> getItems() {
        return items;
    }

    public List<PartItem> getTestData() {
        int[] colors = {Color.parseColor("#FF0000"), Color.parseColor("#0000FF"), Color.parseColor("#EE3B3B"), Color.parseColor("#696969")};
        int[] percents = {15, 25, 35, 25};
        List<PartItem> list = new ArrayList<>();
        PartItem item;
        for (int i = 0; i < 4; i++) {
            item = new PartItem(colors[i], percents[i]);
            list.add(item);
        }
        return list;
    }

    public static class PartItem {
        public int color;
        public int progress;

        public PartItem(int color, int progress) {
            this.color = color;
            this.progress = progress;
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
