package com.style.view.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by xiajun on 2017/7/21.
 */

public class SoundWaveView extends View {
    private static final String TAG = "SoundWaveView";
    private int mWidth;
    private int mHeight;
    private Paint wavePaint;
    //圆心坐标
    private float cx;
    private float cy;
    //最外圆半径
    private int changeRadius;
    //最小半径圆颜色
    private int waveColor = 0xFFFF3030;
    //最大半径圆颜色透明度
    private int maxRadiusAlpha = 10;
    //每像素所占透明度
    private float alphaScale;
    //最大半径
    private int maxRadius;
    //最小半径
    private int minRadius = 10;
    //圆间距
    private int circlePadding = 150;

    Thread animationThread;
    private boolean isStart;

    public SoundWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoundWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaint.setAntiAlias(true);
        wavePaint.setDither(true);
        wavePaint.setStyle(Paint.Style.STROKE);
        wavePaint.setStrokeWidth(2);
        wavePaint.setColor(waveColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged w==" + w + "   h==" + h);
        mWidth = w;
        mHeight = h;
        cx = mWidth / 2;
        cy = mHeight / 2;
        maxRadius = mWidth / 2;
        changeRadius = minRadius;
        alphaScale = (256 - 30 + 0.0f) / (maxRadius - minRadius);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if (!isStart) {
            wavePaint.setColor(waveColor);
            canvas.drawCircle(cx, cy, minRadius, wavePaint);
        } else {
            Log.e(TAG, "---------------------------------");
            int alpha = maxRadiusAlpha;
            for (int r = changeRadius; r > 0; r -= circlePadding) {
                Log.e(TAG, "changeRadius = " + r);
                if (r >= minRadius) {
                    //计算不同半径下的圆透明度
                    alpha = (int) (maxRadiusAlpha + alphaScale * (maxRadius - r));
                    alpha = Math.min(alpha, 256);
                    wavePaint.setColor(alpha << 24 | (0x00FFFFFF & waveColor));
                    canvas.drawCircle(cx, cy, r, wavePaint);
                }
            }
        }
    }

    public void start() {
        isStart = true;
        animationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        changeRadius += 8;
                        if (changeRadius > maxRadius) {
                            changeRadius = maxRadius - circlePadding;
                        }
                        postInvalidate();
                        Thread.sleep(80);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        animationThread.start();
    }

    public void pause() {
        if (animationThread != null && animationThread.isAlive()) {
            animationThread.interrupt();
        }
    }

    public void reset() {
        pause();
        isStart = false;
        invalidate();
        changeRadius = minRadius;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e(TAG, "onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e(TAG, "onDetachedFromWindow");
        reset();
    }
}
