package com.style.view.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xiajun on 2017/7/20.
 */

public class BezierCurve extends View {
    //波浪画笔
    private Paint mPaint;
    //波浪Path类
    private Path mPath;

    private int mHeight;
    private int mWidth;

    public BezierCurve(Context context) {
        super(context);
    }

    public BezierCurve(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BezierCurve(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //Log.e("CustomView", "onSizeChanged");
        mHeight = h;
        mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.e("mWaveCount", mWaveCount+"");
       /* mPath.reset();
        mPath.moveTo(0, mHeight / 2);
        mPath.quadTo(mWidth / 4, mHeight, mWidth / 2, mHeight / 2);
        mPath.quadTo(mWidth / 4 * 3, 0, mWidth, mHeight / 2);*/
        //mPath.lineTo(0, mHeight / 2);
        //mPath.close();
        //canvas.drawPath(mPath, mPaint);
        canvas.drawPath(drawTopRect(40, 40, 400, 400, 40), mPaint);

    }

    //画顶部圆角矩形
    private Path drawTopRect(int left, int top, int right, int bottom, int radius) {
        Path ctx = new Path();
        ctx.reset();
        ctx.moveTo(left, top + radius);
        //画左上角
        ctx.quadTo(left, top, left + radius, top);
        ctx.lineTo(right - radius, top);
        //画右上角
        ctx.quadTo(right, top, right, top + radius);
        ctx.lineTo(right, bottom);
        ctx.lineTo(left, bottom);
        ctx.lineTo(left, top + radius);
        ctx.close();
        return ctx;
    }
}
