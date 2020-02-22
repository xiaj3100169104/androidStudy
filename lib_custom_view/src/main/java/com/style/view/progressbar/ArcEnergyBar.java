package com.style.view.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.style.lib_custom_view.R;

/**
 * 类似仪表盘百分比进度 View
 */
public class ArcEnergyBar extends BaseProgressBar {
    private Paint paint;
    protected Paint textPaint;
    private RectF rectF = new RectF();
    private float strokeWidth = 20;
    private String mBottomText = "剩余能量";
    private float arcFinishedStartAngle;
    private int finishedStrokeColor = Color.RED;
    private int unfinishedStrokeColor = Color.rgb(72, 106, 176);

    private int arcTotalSweepAngle = 300;
    private SweepGradient mShader;
    private Paint mFinishPaint;
    private TextPaint mCenterTextPaint;

    public ArcEnergyBar(Context context) {
        this(context, null);
    }

    public ArcEnergyBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcEnergyBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0);
        finishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_finished_color, finishedStrokeColor);
        unfinishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_unfinished_color, unfinishedStrokeColor);
        arcFinishedStartAngle = attributes.getFloat(R.styleable.ArcProgress_arc_finished_start_angle, arcFinishedStartAngle);
        arcTotalSweepAngle = attributes.getInt(R.styleable.ArcProgress_arc_total_sweep_angle, arcTotalSweepAngle);
        int progress = attributes.getInt(R.styleable.ArcProgress_arc_progress, 0);
        setProgress(progress);
        strokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width, strokeWidth);
        attributes.recycle();

        initPainters(context);
    }

    protected void initPainters(Context context) {
        paint = new Paint();
        paint.setColor(unfinishedStrokeColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        mFinishPaint = new Paint(paint);
        mFinishPaint.setColor(finishedStrokeColor);
        //这个东西有bug，边角为圆角时渐变色有问题
        mShader = new SweepGradient(dp2px(context, 150) / 2, dp2px(context, 150) / 2, finishedStrokeColor, Color.rgb(66, 145, 241));
        /*Matrix gradientMatrix = new Matrix();
        gradientMatrix.preRotate(90, Utils.dp2px(context, 150) / 2, Utils.dp2px(context, 150) / 2);
        mShader.setLocalMatrix(gradientMatrix);*/
        mFinishPaint.setShader(mShader);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(finishedStrokeColor);
        textPaint.setTextSize(sp2px(context, 12));

        mCenterTextPaint = new TextPaint(textPaint);
        mCenterTextPaint.setTextSize(sp2px(context, 15));
        mCenterTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        rectF.set(strokeWidth / 2f, strokeWidth / 2f, width - strokeWidth / 2f, MeasureSpec.getSize(heightMeasureSpec) - strokeWidth / 2f);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        //以画布中心点旋转坐标系
        canvas.rotate(arcFinishedStartAngle, getWidth() / 2, getHeight() / 2);
        float finishedSweepAngle = getProgress() / (float) getMax() * arcTotalSweepAngle;
        canvas.drawArc(rectF, 0, arcTotalSweepAngle, false, paint);
        canvas.drawArc(rectF, 0, finishedSweepAngle, false, mFinishPaint);
        canvas.restore();

        canvas.save();
        canvas.translate(0, getHeight());
        float mBottomTextHeight = getTextHeight(textPaint.getFontMetrics());
        float mBottomTextWidth = textPaint.measureText(mBottomText);
        canvas.drawText(mBottomText, getWidth() / 2 - mBottomTextWidth / 2, -(mBottomTextHeight / 2 - getBaseLine2CenterY(textPaint.getFontMetrics())) - dp2px(getContext(), 10), textPaint);
        canvas.restore();

        canvas.save();
        canvas.translate(0, 0);
        float mCenterTextWidth = mCenterTextPaint.measureText(mBottomText);
        canvas.drawText(mBottomText, getWidth() / 2 - mCenterTextWidth / 2, getHeight() / 2 + getBaseLine2CenterY(mCenterTextPaint.getFontMetrics()), mCenterTextPaint);
        canvas.restore();
    }
}
