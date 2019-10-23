package com.style.view.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.style.framework.R;
import com.style.utils.DeviceInfoUtil;

/**
 * Created by xiajun on 2017/7/21.
 */

public class WaterPoloProgress extends View {
    private static final String TAG = "WaterPoloProgress";
    private final static String DEFAULT_PERCENT_TAG = "%";

    private int mWidth;
    private int mHeight;
    private Paint borderPaint;
    private Paint circlePaint;
    //波浪画笔
    private Paint wavePaint;
    //中心文本画笔
    private Paint centerTextPaint;
    private Path path;
    //圆心坐标
    private float x;
    private float y;
    //圆的半径
    private float radius;
    //水轮廓路径
    private Path wavePath;

    //波峰、波谷拉长倍数
    private float amplitude = 20;//振幅
    //同一时刻显示多少个波长
    private float frequency = 2;//频率
    //百分比默认为50
    private float mCurPercent = 50;
    //开始角度，默认90度
    private int startAngle;
    private int circleColor = 0x25FF3030;
    private int waterColor = 0x70FF3030;
    private int borderColor = 0x90FF3030;
    private float borderWidth;
    //不确定进度
    private boolean mIndeterminate = true;
    //中心文本颜色
    private int mTextColor = 0xff333333;
    //中心文本字体大小
    private float mCenterTextSize;
    //百分比后缀
    private String percentTag = DEFAULT_PERCENT_TAG;
    //中心文本
    private String centerText = "";
    //是否显示中心文本
    private boolean showText = true;
    private PercentThread percentThread;
    private HorizontalMoveThread moveThread;

    public WaterPoloProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterPoloProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        borderWidth = DeviceInfoUtil.dp2px(context, 3);
        mCenterTextSize = DeviceInfoUtil.sp2px(context, 20);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaterPoloProgress, defStyleAttr, 0);
        circleColor = a.getColor(R.styleable.WaterPoloProgress_waterPolo_circleColor, circleColor);
        waterColor = a.getColor(R.styleable.WaterPoloProgress_waterPolo_waterColor, waterColor);
        borderColor = a.getColor(R.styleable.WaterPoloProgress_waterPolo_borderColor, borderColor);
        startAngle = a.getInt(R.styleable.WaterPoloProgress_waterPolo_startAngle, 90);
        borderWidth = a.getDimension(R.styleable.WaterPoloProgress_waterPolo_borderWidth, borderWidth);
        mCurPercent = a.getInt(R.styleable.WaterPoloProgress_waterPolo_waterPercent, (int) mCurPercent);
        mIndeterminate = a.getBoolean(R.styleable.WaterPoloProgress_waterPolo_indeterminate, mIndeterminate);
        mTextColor = a.getColor(R.styleable.WaterPoloProgress_waterPolo_centerTextColor, mTextColor);
        mCenterTextSize = a.getDimension(R.styleable.WaterPoloProgress_waterPolo_centerTextSize, mCenterTextSize);

        a.recycle();

        initPaint();
        path = new Path();
        wavePath = new Path();
        //setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private void initPaint() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(circleColor);
        wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaint.setAntiAlias(true);
        wavePaint.setDither(true);
        wavePaint.setStyle(Paint.Style.FILL);
        wavePaint.setColor(waterColor);
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setAntiAlias(true);
        borderPaint.setDither(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        centerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerTextPaint.setAntiAlias(true);
        centerTextPaint.setDither(true);
        centerTextPaint.setColor(mTextColor);
        centerTextPaint.setTextSize(mCenterTextSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        radius = mWidth / 2;
        x = mWidth / 2;
        y = mHeight / 2;
        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        //裁剪成圆区域
        path.reset();
        path.addCircle(mWidth / 2, mHeight / 2, radius, Path.Direction.CCW);
        //9.0只允许Region.Op.INTERSECT && op != Region.Op.DIFFERENCE
        canvas.clipPath(path);

        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, circlePaint);

        //绘制之前记得要重置路径
        wavePath.reset();
        //注意百分比越大，y应该越小
        float waveHeight = mCurPercent / 100 * mHeight;
        //降角度值转化为Math.PI形式,
        //特别注意startAngle要转化为小数形式，不然结果会有问题
        double startAnglePI = ((double) startAngle / 360.00) * Math.PI * 2;
        float startX = 0;
        float startY = 0;
        for (double i = 0; i < mWidth; i++) {
            float y1 = (float) (amplitude * Math.sin(i / mWidth * (2 * Math.PI * frequency) + startAnglePI));
            //横坐标
            float x = (float) i;
            //纵坐标
            float y = mHeight - (waveHeight - y1);

            if (i == 0) {
                startX = x;
                startY = y;
                wavePath.moveTo(startX, y);
            } else {
                wavePath.lineTo(x, y);
            }
        }
        wavePath.lineTo(mWidth, mHeight);
        wavePath.lineTo(0, mHeight);
        wavePath.lineTo(startX, startY);
        canvas.drawPath(wavePath, wavePaint);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, borderPaint);
        //绘制文本
        if (showText) {
            setCenterText((int) mCurPercent + getPercentTag());
            String text = getCenterText();
            float textLength = centerTextPaint.measureText(text);
            float textHeight = centerTextPaint.descent();// + textPaint.ascent();
            canvas.drawText(text, x - textLength / 2, y + textHeight, centerTextPaint);
        }
    }

    public void setShowText(boolean showText) {
        this.showText = showText;
    }

    public String getPercentTag() {
        return percentTag;
    }

    public void setPercentTag(String percentTag) {
        this.percentTag = percentTag;
    }

    public String getCenterText() {
        return centerText;
    }

    public void setCenterText(String centerText) {
        this.centerText = centerText;
    }

    public void setPercentWithAnimation(float percent) {
        if (percent > 100) {
            throw new IllegalArgumentException("percent must less than 100!");
        }
        if (moveThread != null && moveThread.isAlive()) {
            moveThread.setStop();
            moveThread.interrupt();
        }
        if (percentThread != null && percentThread.isAlive()) {
            percentThread.setStop();
            percentThread.interrupt();
        }
        percentThread = new PercentThread(percent);
        percentThread.start();
    }

    public void horizontalMove(int startAngle) {
        if (moveThread != null && moveThread.isAlive()) {
            moveThread.setStop();
            moveThread.interrupt();
        }
        moveThread = new HorizontalMoveThread(startAngle);
        moveThread.start();
    }

    private class PercentThread extends Thread {
        private float percent;
        private boolean canContinue = true;

        public PercentThread(float percent) {
            this.percent = percent;
        }

        @Override
        public void run() {
            int sleepTime = 1;
            for (int i = 0; i <= this.percent; i++) {
                try {
                    if (canContinue) {
                        if (i % 20 == 0) {
                            sleepTime += 2;
                        }
                        mCurPercent = i;
                        //向上的同时向右移动
                        startAngle = -i;
                        postInvalidate();
                        Thread.sleep(sleepTime);
                        if (i == this.percent) {
                            if (canContinue)
                                horizontalMove(i + 1);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void interrupt() {
            super.interrupt();
        }

        public void setStop() {
            canContinue = false;
            if (moveThread != null) {
                moveThread.setStop();
            }
        }
    }

    private class HorizontalMoveThread extends Thread {
        private int startAngle;
        private boolean canContinue = true;

        public HorizontalMoveThread(int startAngle) {
            this.startAngle = startAngle;
        }

        @Override
        public void run() {
            for (int i = startAngle; i < 360; i++) {
                try {
                    if (canContinue) {
                        WaterPoloProgress.this.startAngle = -i;
                        postInvalidate();
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
        if (moveThread != null && moveThread.isAlive()) {
            moveThread.interrupt();
        }
        percentThread = null;
        moveThread = null;
        System.gc();
    }


}
