package com.style.view.other;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.style.lib_custom_view.R;
import com.style.view.progressbar.BaseProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 饼状图
 */
public class PieChartView2 extends BaseProgressBar {

    private final float default_stroke_width;
    private final float default_src_offset = 0f;
    private Paint piepaint;
    private Paint outerLinePaint;
    private RectF rectF = new RectF();

    private float srcRadius;
    private float srcOffset;
    private float strokeWidth;
    private float arcFinishedStartAngle = 0;
    private List<PartItem> items;
    private int mViewWidth;
    private int mViewHeight;

    public PieChartView2(Context context) {
        this(context, null);
    }

    public PieChartView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        default_stroke_width = dp2px(context, 4);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PieChart, defStyleAttr, 0);
        arcFinishedStartAngle = attributes.getFloat(R.styleable.PieChart_pie_chart_finished_start_angle, arcFinishedStartAngle);
        srcRadius = attributes.getDimension(R.styleable.PieChart_pie_chart_arc_radius, 0);
        srcOffset = attributes.getDimension(R.styleable.PieChart_pie_chart_arc_offset, default_src_offset);
        strokeWidth = attributes.getDimension(R.styleable.PieChart_pie_chart_stroke_width, default_stroke_width);
        attributes.recycle();
        initPainters();
        setItems(getTestData());
    }

    protected void initPainters() {
        piepaint = new Paint();
        piepaint.setAntiAlias(true);
        piepaint.setStyle(Paint.Style.FILL);
        outerLinePaint = new Paint();
        outerLinePaint.setAntiAlias(true);
        outerLinePaint.setStyle(Paint.Style.STROKE);
        outerLinePaint.setStrokeWidth(srcOffset);
        outerLinePaint.setColor(Color.WHITE);
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
        //饼状图矩形区域,注意这里的矩形边界是指矩形边框的中间位置
        //rectF.set(strokeWidth / 1f, strokeWidth / 1f, mViewWidth - strokeWidth / 1f, mViewHeight - strokeWidth / 1f);
        rectF.set((mViewWidth - srcRadius * 2) / 2, (mViewHeight - srcRadius * 2) / 2, (mViewWidth - srcRadius * 2) / 2 + srcRadius * 2, (mViewHeight - srcRadius * 2) / 2 + srcRadius * 2);

        float innerRadius = srcRadius - strokeWidth;
        int startAngle = 0;
        for (int i = 0; i < data.size(); i++) {
            PartItem item = data.get(i);
            piepaint.setColor(item.color);
            float itemSweepAngle = item.progress / (float) getMax() * 360;
            //最后一条数据特殊处理
            if (i == data.size() - 1)
                itemSweepAngle = 360 - startAngle;
            float progressAngle = getProgress() / (float) getMax() * 360;
            //有动画时
            if (startAngle + itemSweepAngle > progressAngle) {
                itemSweepAngle = progressAngle - startAngle;
                canvas.drawArc(rectF, startAngle, itemSweepAngle, true, piepaint);
                break;
            }
            canvas.drawArc(rectF, startAngle, itemSweepAngle, true, piepaint);
            outerLinePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, innerRadius, outerLinePaint);
            if (srcOffset > 0) {
                outerLinePaint.setStyle(Paint.Style.STROKE);
                outerLinePaint.setStrokeWidth(srcOffset);
                canvas.drawArc(rectF, startAngle, itemSweepAngle, true, outerLinePaint);
            }
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
        int[] colors = {Color.parseColor("#FFA500"), Color.parseColor("#FFD700")
                , Color.parseColor("#BCEE68"), Color.parseColor("#008B00")
                , Color.parseColor("#00868B"), Color.parseColor("#1C86EE")
                , Color.parseColor("#CD2990"), Color.parseColor("#CD0000")
                , Color.parseColor("#CD4F39")};
        int[] percents = {46, 14, 8, 8, 8, 5, 5, 4, 2};
        List<PartItem> list = new ArrayList<>();
        PartItem item;
        for (int i = 0; i < 9; i++) {
            item = new PartItem(colors[i], percents[i]);
            list.add(item);
        }
        return list;
    }

    public List<PartItem> getTestData2() {
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
