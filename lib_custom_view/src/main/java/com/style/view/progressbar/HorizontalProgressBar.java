package com.style.view.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.style.lib_custom_view.R;


public class HorizontalProgressBar extends View {
    private static final String TAG = "HorizontalProgressBar";
    private final float roundCornerRadius;
    private int mBackgroundColor = Color.GRAY;
    private int mProgressColor = Color.GREEN;
    private int progress = 50;
    private Paint mPaint;
    private final RectF allRect;
    private RectF progressRect;
    private int width;
    private int height;

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBar(Context context) {
        this(context, null);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBar, defStyle, 0);
        mBackgroundColor = a.getColor(R.styleable.HorizontalProgressBar_horizontalProgressBackgroundColor, mBackgroundColor);
        mProgressColor = a.getColor(R.styleable.HorizontalProgressBar_horizontalProgressColor, mProgressColor);
        progress = a.getInteger(R.styleable.HorizontalProgressBar_horizontalProgress, progress);
        roundCornerRadius = a.getDimension(R.styleable.HorizontalProgressBar_horizontalProgressRoundCornerRadius, 0);
        a.recycle();
        initPaint();
        allRect = new RectF();
        progressRect = new RectF();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);//设置填满
        mPaint.setColor(mProgressColor);
        mPaint.setAntiAlias(true);
        //mPaint.setFilterBitmap(true);
    }

    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    public void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("progress must less than the max!");
        }
        this.progress = progress;
        invalidate();
    }

    public void setCacheColor(int color) {
        invalidate();
    }

    public void setProgressColor(int color) {
        mProgressColor = color;
        mPaint.setColor(mProgressColor);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        //画背景
        allRect.set(0, 0, width, height);
        mPaint.setColor(mBackgroundColor);
        canvas.drawRoundRect(allRect, roundCornerRadius, roundCornerRadius, mPaint);
        //画进度
        float progressRight = progress / 100f * width;
        progressRect.set(0, 0, progressRight, height);
        mPaint.setColor(mProgressColor);
        canvas.drawRoundRect(progressRect, roundCornerRadius, roundCornerRadius, mPaint);
    }
}
