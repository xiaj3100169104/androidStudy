package com.style.view.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xiajun on 2017/7/21.
 */

public class SineCurve extends View {
    private static final String TAG = "SineCurve";
    private Paint paint;
    private Path mPath;
    private int mWidth;
    private int mHeight;
    //默认为   mHeight/2
    private float waveHeight;

    //波峰、波谷拉长倍数
    private float amplitude = 50;//振幅
    //指定宽度内绘制多少个完整波
    private float frequency = 2;//频率
    //开始角度
    private double startAngle = 0.00;

    public SineCurve(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        mPath = new Path();
        //setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        waveHeight = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        double startRadians = Math.toRadians(startAngle);
        double scale = (2 * Math.PI * frequency) / mWidth;
        for (double i = 0; i < mWidth; i++) {
            double y1 = amplitude * Math.sin(i * scale - startRadians);
            float x = (float) i;
            float y = waveHeight - (float) y1;
            //Log.e(TAG, "x=" + x + "   y=" + y);
            if (i == 0) {
                mPath.moveTo(x, y);
            } else
                mPath.lineTo(x, y);

        }
        canvas.drawPath(mPath, paint);
    }
}
