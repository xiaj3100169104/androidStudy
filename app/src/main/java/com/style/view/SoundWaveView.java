package com.style.view;

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
    //波浪画笔
    private Paint wavePaint;
    //圆心坐标
    private float cx;
    private float cy;
    //外圆半径
    private int changeRadius;
    //波浪宽度
    private int waveWidth = 40;
    private int waveColor = 0x10FF3030;
    //最大半径
    private int maxRadius;
    Thread animationThread;

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
        wavePaint.setStyle(Paint.Style.FILL);
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
        changeRadius = maxRadius - waveWidth;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        Log.e(TAG, "onDraw maxRadius==" + maxRadius);
        wavePaint.setColor(waveColor);
        canvas.drawCircle(cx, cy, maxRadius, wavePaint);
        int changeColor = waveColor;
        for (int i = changeRadius; i > 0; i -= waveWidth) {
            changeColor += 0x08000000;
            wavePaint.setColor(changeColor);
            canvas.drawCircle(cx, cy, i, wavePaint);
        }

    }

    public void startAnimation() {
        animationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //注意：此时可能view还没绘制出来，宽度为0，所以需要延迟一下,除非界面延迟执行
                    //Thread.sleep(1000);
                    int minR = maxRadius - waveWidth;
                    Log.e(TAG, "run mWidth==" + mWidth);
                    for (int i = minR; i <= maxRadius; i+=5) {
                        changeRadius = i;
                        postInvalidate();
                        Thread.sleep(100);
                        if (i >= maxRadius) {
                            i = minR;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        animationThread.start();
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
        if (animationThread != null && animationThread.isAlive()) {
            animationThread.interrupt();
        }
    }
}
