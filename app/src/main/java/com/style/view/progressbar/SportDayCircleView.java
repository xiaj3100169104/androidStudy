package com.style.view.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.style.framework.R;

/**
 * 类似仪表盘百分比进度 View
 */
public class SportDayCircleView extends BaseProgressBar {
    private Paint paint;
    protected Paint textPaint;

    private RectF rectF = new RectF();

    private float strokeWidth;
    private float suffixTextSize;
    private float bottomTextSize;
    private String bottomText;
    private float textSize;
    private int textColor;
    private int max;
    private int finishedStrokeColor;
    private int unfinishedStrokeColor;
    private int smallCircleStrokeColor = 0x99CCCCCC;
    private int smallCircleColor = 0xFFFFFFFF;
    private float startAngle = 0;
    private float arcAngle;
    private String suffixText = "%";
    private float suffixTextPadding;

    private float arcBottomHeight;

    private final int default_finished_color = Color.WHITE;
    private final int default_unfinished_color = Color.rgb(72, 106, 176);
    private final int default_text_color = Color.rgb(66, 145, 241);
    private final float default_suffix_text_size;
    private final float default_suffix_padding;
    private final float default_bottom_text_size;
    private final float default_stroke_width;
    private final String default_suffix_text;
    private final int default_max = 100;
    private final float default_arc_angle = 360f;
    private float default_text_size;
    private final int min_size;

    private float radius;

    public SportDayCircleView(Context context) {
        this(context, null);
    }

    public SportDayCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SportDayCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_text_size = Utils.sp2px(context, 18);
        min_size = (int) Utils.dp2px(context, 100);
        default_text_size = Utils.sp2px(context, 40);
        default_suffix_text_size = Utils.sp2px(context, 15);
        default_suffix_padding = Utils.dp2px(context, 4);
        default_suffix_text = "%";
        default_bottom_text_size = Utils.sp2px(context, 10);
        default_stroke_width = Utils.dp2px(context, 4);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
    }

    protected void initByAttributes(TypedArray attributes) {
        finishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_finished_color, default_finished_color);
        unfinishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_unfinished_color, default_unfinished_color);
        textColor = attributes.getColor(R.styleable.ArcProgress_arc_text_color, default_text_color);
        textSize = attributes.getDimension(R.styleable.ArcProgress_arc_text_size, default_text_size);
        arcAngle = attributes.getFloat(R.styleable.ArcProgress_arc_angle, default_arc_angle);
        setMax(attributes.getInt(R.styleable.ArcProgress_arc_max, default_max));
        setProgress(attributes.getInt(R.styleable.ArcProgress_arc_progress, 0));
        strokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width, default_stroke_width);
        suffixTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_size, default_suffix_text_size);
        suffixText = TextUtils.isEmpty(attributes.getString(R.styleable.ArcProgress_arc_suffix_text)) ? default_suffix_text : attributes.getString(R.styleable.ArcProgress_arc_suffix_text);
        suffixTextPadding = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_padding, default_suffix_padding);
        bottomTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_bottom_text_size, default_bottom_text_size);
        bottomText = attributes.getString(R.styleable.ArcProgress_arc_bottom_text);
    }

    protected void initPainters() {
        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        paint = new Paint();
        paint.setColor(default_unfinished_color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return min_size;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return min_size;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        rectF.set(strokeWidth / 2f, strokeWidth / 2f, width - strokeWidth / 2f, MeasureSpec.getSize(heightMeasureSpec) - strokeWidth / 2f);
        radius = width / 2f;
        float angle = (360 - arcAngle) / 2f;
        arcBottomHeight = radius * (float) (1 - Math.cos(angle / 180 * Math.PI));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float finishedSweepAngle = getProgress() / (float) getMax() * arcAngle;
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(unfinishedStrokeColor);
        canvas.drawArc(rectF, startAngle, arcAngle, false, paint);
        paint.setColor(finishedStrokeColor);
        canvas.drawArc(rectF, startAngle, finishedSweepAngle, false, paint);
        canvas.save();

        canvas.translate(radius, radius);
        float r = radius - strokeWidth / 2;
        //弧度，单位π
        double radian = finishedSweepAngle / 180f * Math.PI;
        float x = (float) (r * Math.cos(radian));
        float y = (float) (r * Math.sin(radian));
        float r2 = strokeWidth / 2 + strokeWidth / 3;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(smallCircleColor);
        canvas.drawCircle(x, y, r2, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(smallCircleStrokeColor);
        canvas.drawCircle(x, y, r2, paint);
        canvas.restore();

    }
}
