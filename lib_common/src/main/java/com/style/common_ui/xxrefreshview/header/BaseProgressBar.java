package com.style.common_ui.xxrefreshview.header;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by xiajun on 2017/7/31.
 */

public abstract class BaseProgressBar extends View {
    private static final String TAG = "BaseProgressBar";

    //进度
    protected int progress = 0;
    private PercentThread percentThread;

    public BaseProgressBar(Context context) {
        super(context);
    }
    public BaseProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //带动画渐进到指定进度
    public void setPercentWithAnimation(int percent) {
        if (percent > 100) {
            throw new IllegalArgumentException("percent must less than 100!");
        }
        if (percentThread != null && percentThread.isAlive()) {
            //停止上一次绘制线程，否则之前可能还在绘制
            percentThread.setStop();
            percentThread.interrupt();
        }
        percentThread = new PercentThread(percent);
        percentThread.setPercent(percent);
        percentThread.start();
    }

    private class PercentThread extends Thread {
        private int percent;
        private boolean canContinue = true;

        public PercentThread(int percent) {
            this.percent = percent;
        }

        public void setPercent(int percent) {
            this.percent = percent;
        }

        @Override
        public void run() {
            int sleepTime = 1;
            for (int i = 0; i <= percent; i++) {
                if (canContinue) {
                    if (i % 20 == 0) {
                        sleepTime += 2;
                    }
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progress = i;
                    postInvalidate();
                }
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
}
