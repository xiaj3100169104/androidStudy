package com.style.view.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TimeUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.style.framework.R;

/**
 * 百分比圆环形进度 View
 */
public class CirclePercentView extends View {
    private final static int UPDATE_PROGRESS = 2;

    //总体大小
    private int mHeight;
    private int mWidth;
    //圆的半径
    private float mRadius;
    //圆心坐标
    private float x;
    private float y;
    //不确定进度
    private boolean mIndeterminate;
    //大圆颜色
    private int mBigColor;
    //扇形进度颜色
    private int mfinishColor;
    //中心文本颜色
    private int mtextColor;
    //进度条宽度
    private float mStripeWidth;
    //动画位置百分比进度
    private int mCurPercent;
    //要画的弧度跨度
    private int mSweepAngle;
    //小圆的颜色
    private int mSmallColor;

    //中心百分比文字大小
    private float mCenterTextSize;

    public CirclePercentView(Context context) {
        this(context, null);
    }

    public CirclePercentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView, defStyleAttr, 0);
        mStripeWidth = a.getDimension(R.styleable.CirclePercentView_stripeWidth, Utils.dpToPx(30, context));
        mCurPercent = a.getInteger(R.styleable.CirclePercentView_percent, 0);
        mBigColor = a.getColor(R.styleable.CirclePercentView_bigColor, 0xffE5E5E5);
        mfinishColor = a.getColor(R.styleable.CirclePercentView_finishColor, 0xff00BFFF);
        mSmallColor = a.getColor(R.styleable.CirclePercentView_smallColor, 0xffffffff);
        mtextColor = a.getColor(R.styleable.CirclePercentView_textColor, 0xff333333);
        mCenterTextSize = a.getDimensionPixelSize(R.styleable.CirclePercentView_centerTextSize, Utils.spToPx(20, context));
        mRadius = a.getDimensionPixelSize(R.styleable.CirclePercentView_radius, Utils.dpToPx(50, context));
        mIndeterminate = a.getBoolean(R.styleable.CirclePercentView_indeterminate, true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取测量大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            mRadius = widthSize / 2;
            x = widthSize / 2;
            y = heightSize / 2;
            mWidth = widthSize;
            mHeight = heightSize;
        }

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            mWidth = (int) (mRadius * 2);
            mHeight = (int) (mRadius * 2);
            x = mRadius;
            y = mRadius;

        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIndeterminate)//不确定进度，加旋转动画
            setCurPercent(15);
        mSweepAngle = (int) (getCurPercent() * 3.6);
        //绘制大圆
        Paint bigCirclePaint = new Paint();
        bigCirclePaint.setAntiAlias(true);
        bigCirclePaint.setColor(mBigColor);
        canvas.drawCircle(x, y, mRadius, bigCirclePaint);

        //饼状图
        Paint sectorPaint = new Paint();
        sectorPaint.setColor(mfinishColor);
        sectorPaint.setAntiAlias(true);
        RectF rect = new RectF(0, 0, mWidth, mHeight);
        canvas.drawArc(rect, 270, mSweepAngle, true, sectorPaint);

        //绘制小圆,颜色透明
        Paint smallCirclePaint = new Paint();
        smallCirclePaint.setAntiAlias(true);
        smallCirclePaint.setColor(mSmallColor);
        canvas.drawCircle(x, y, mRadius - mStripeWidth, smallCirclePaint);

        //绘制文本
        Paint textPaint = new Paint();
        String text = mCurPercent + "%";
        textPaint.setColor(mtextColor);
        textPaint.setTextSize(mCenterTextSize);
        float textLength = textPaint.measureText(text);
        float textHeight = textPaint.descent();// + textPaint.ascent();
        canvas.drawText(text, x - textLength / 2, y + textHeight, textPaint);


    }


    public void startAnimation() {
        if (mIndeterminate){
            RotateAnimation animation =new RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            animation.setDuration(500);//设置动画持续时间
/** 常用方法 */
            animation.setRepeatCount(5);//设置重复次数
            setAnimation(animation);
            super.startAnimation(animation);

        }
    }

    //设置进度并立即刷新
    public void setCurPercent(int percent) {
        if (percent <= 100) {
            mCurPercent = percent;
            this.invalidate();
        }
    }

    public int getCurPercent() {
        return mCurPercent;
    }

    //带动画渐进到指定进度
    public void setPercentWithAnimation(final int percent) {
        if (percent > 100) {
            throw new IllegalArgumentException("percent must less than 100!");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 1;
                for (int i = 0; i <= percent; i++) {
                    if (i % 20 == 0) {
                        sleepTime += 2;
                    }
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mCurPercent = i;
                    postInvalidate();
                }
            }
        }).start();
    }
}
