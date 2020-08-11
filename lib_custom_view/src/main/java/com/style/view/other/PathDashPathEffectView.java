package com.style.view.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xiajun on 2017/7/20.
 */

public class PathDashPathEffectView extends View {
    //波浪画笔
    private Paint mPaint;
    //波浪Path类
    private Path mPath;

    private int mHeight;
    private int mWidth;

    public PathDashPathEffectView(Context context) {
        super(context);
    }

    public PathDashPathEffectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PathDashPathEffectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关掉硬件加速
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        // 初始化PathDashPathEffect
        Path p = new Path();
        p.addRect(0, 0, 20, 20, Path.Direction.CCW);
        PathDashPathEffect effects = new PathDashPathEffect(p, 50, 10, PathDashPathEffect.Style.TRANSLATE);
        mPaint.setPathEffect(effects);

        mPath.reset();
        mPath.moveTo(50, 50);
        mPath.lineTo(400, 400);
        mPath.lineTo(750, 50);
        mPath.lineTo(1200, 400);
        //mPath.close();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }
}
