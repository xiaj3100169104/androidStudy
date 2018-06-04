package com.style.view;

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
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
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

        mPath.reset();
        mPath.moveTo(0, mHeight / 2);
        mPath.quadTo(mWidth / 4, mHeight, mWidth / 2, mHeight / 2);
        mPath.quadTo(mWidth / 4 * 3, 0, mWidth, mHeight / 2);
        mPath.lineTo(0, mHeight / 2);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }
}
