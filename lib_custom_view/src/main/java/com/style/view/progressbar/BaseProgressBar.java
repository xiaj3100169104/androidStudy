package com.style.view.progressbar;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * 进度相关view基类
 * Created by xiajun on 2017/7/31.
 */

public abstract class BaseProgressBar extends View {
    private static final String TAG = "BaseProgressBar";
    protected static final int DEFAULT_MAX = 100;
    private int max = DEFAULT_MAX;
    //进度
    private int progress = 0;
    private PercentThread percentThread;

    public BaseProgressBar(Context context) {
        super(context);
    }

    public BaseProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (this.progress > getMax()) {
            throw new IllegalArgumentException("progress must less than the max!");
        }
        this.progress = progress;
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    //带动画渐进到指定进度
    public void setProgressWithAnimation(int progress) {
        if (progress > getMax()) {
            throw new IllegalArgumentException("progress must less than the max!");
        }
        if (percentThread != null && percentThread.isAlive()) {
            //停止上一次绘制线程，否则之前可能还在绘制
            percentThread.setStop();
            percentThread.interrupt();
        }
        percentThread = new PercentThread(progress);
        percentThread.start();
    }

    private class PercentThread extends Thread {
        private int percent;
        private boolean canContinue = true;

        public PercentThread(int percent) {
            this.percent = percent;
        }

        @Override
        public void run() {
            try {
                //间隔时间太短根本看不出动画效果
                int sleepTime = 0;
                for (int i = 0; i <= percent; i++) {
                    if (canContinue) {
                        if (i % 20 == 0) {
                            sleepTime += 2;
                        }
                        Thread.sleep(sleepTime);
                        progress = i;
                        postInvalidate();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void setStop() {
            canContinue = false;
        }
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
        if (percentThread != null && percentThread.isAlive()) {
            percentThread.interrupt();
        }
        percentThread = null;
        System.gc();
    }

    /**
     * 计算绘制文字时的基线到中轴线的距离
     *
     * @param fontMetrics
     * @return 基线和centerY的距离
     */
    public static float getBaseLine2CenterY(Paint.FontMetrics fontMetrics) {
        return (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
    }

    public float getTextHeight(Paint.FontMetrics fontMetrics) {
        return Math.abs(fontMetrics.descent - fontMetrics.ascent);
    }

    public static int dp2px(Context context, float dpValue) {
        float pxValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
        return (int) (pxValue + 0.5f);//0.5f是为了四舍五入，但有时候四舍五入不一定就好
    }

    public static float sp2px(Context context, int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

}
