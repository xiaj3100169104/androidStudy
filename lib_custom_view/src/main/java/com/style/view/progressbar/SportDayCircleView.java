package com.style.view.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.style.lib_custom_view.R;

/**
 * 类似仪表盘百分比进度 View
 */
public class SportDayCircleView extends BaseProgressBar {
    private Paint paint;
    protected Paint textPaint;
    private RectF rectF = new RectF();

    private float strokeWidth;
    private int finishedStrokeColor;
    private int unfinishedStrokeColor;
    private int smallCircleStrokeColor = 0x99CCCCCC;
    private int smallCircleColor = 0xFFFFFFFF;
    private final float smallCircleRasius;

    private float arcFinishedStartAngle = 0;

    private final int default_finished_color = Color.WHITE;
    private final int default_unfinished_color = Color.rgb(72, 106, 176);
    private final float default_stroke_width;
    private float radius;

    public SportDayCircleView(Context context) {
        this(context, null);
    }

    public SportDayCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SportDayCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        default_stroke_width = dp2px(context, 4);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
        smallCircleRasius = strokeWidth / 2 + strokeWidth / 3;

        initPainters();
    }

    protected void initByAttributes(TypedArray attributes) {
        arcFinishedStartAngle = attributes.getFloat(R.styleable.ArcProgress_arc_finished_start_angle, arcFinishedStartAngle);
        finishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_finished_color, default_finished_color);
        unfinishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_unfinished_color, default_unfinished_color);
        setMax(attributes.getInt(R.styleable.ArcProgress_arc_max, DEFAULT_MAX));
        setProgress(attributes.getInt(R.styleable.ArcProgress_arc_progress, 0));
        strokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width, default_stroke_width);
    }

    protected void initPainters() {
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);

        paint = new Paint();
        paint.setColor(default_unfinished_color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //饼状图矩形区域,注意这里的矩形边界是指矩形边框的中间位置
        rectF.set(strokeWidth / 1f, strokeWidth / 1f, width - strokeWidth / 1f, height - strokeWidth / 1f);
        //小圆中心点到饼状图中心点距离，注意这里半径，不然小圆在90度的倍数时显示不全
        radius = width / 2f - strokeWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        //以画布中心点旋转坐标系
        canvas.rotate(arcFinishedStartAngle, getWidth() / 2, getHeight() / 2);
        float finishedSweepAngle = getProgress() / (float) getMax() * 360;
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(unfinishedStrokeColor);
        canvas.drawArc(rectF, 0, 360, false, paint);
        paint.setColor(finishedStrokeColor);
        canvas.drawArc(rectF, 0, finishedSweepAngle, false, paint);

        canvas.translate(getWidth() / 2f, getWidth() / 2f);
        //弧度，单位π
        double radian = finishedSweepAngle / 180f * Math.PI;
        float x = (float) (radius * Math.cos(radian));
        float y = (float) (radius * Math.sin(radian));
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(smallCircleColor);
        canvas.drawCircle(x, y, smallCircleRasius, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(smallCircleStrokeColor);
        canvas.drawCircle(x, y, smallCircleRasius, paint);
        canvas.restore();

    }
}
