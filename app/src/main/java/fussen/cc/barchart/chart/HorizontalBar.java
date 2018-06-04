package fussen.cc.barchart.chart;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.style.framework.ChartAnimator;
import com.style.framework.R;


/**
 * Created by Fussen on 2017/4/24.
 */

public class HorizontalBar extends View {

    private static final String TAG = "HorizontalBar";
    private int mHeight;
    private int mWidth;

    private float mProgress;
    private ChartAnimator mAnimator;
    private Paint mPaint;
    private int mBackgroundColor = Color.parseColor("#f6f7f8");
    private int mProgressColor = Color.parseColor("#FF6933");


    public HorizontalBar(Context context) {
        super(context);
        initView();
    }


    public HorizontalBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initView();
    }


    public HorizontalBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HorizontalBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
        initView();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray attributes =
                context.obtainStyledAttributes(attrs, R.styleable.Heart_View);
        mBackgroundColor = attributes.getColor(R.styleable.Heart_View_background_color, mBackgroundColor);
        mProgressColor = attributes.getColor(R.styleable.Heart_View_progress_color, mProgressColor);

    }


    private void initView() {

        setBackgroundColor(mBackgroundColor);
        //初始化动画
        mAnimator = new ChartAnimator(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // ViewCompat.postInvalidateOnAnimation(Chart.this);
                postInvalidate();
            }
        });


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mProgressColor);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //宽度的模式
        int mWidthModle = MeasureSpec.getMode(widthMeasureSpec);
        //宽度大小
        int mWidthSize = MeasureSpec.getSize(widthMeasureSpec);

        int mHeightModle = MeasureSpec.getMode(heightMeasureSpec);
        int mHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        //如果明确大小,直接设置大小
        if (mWidthModle == MeasureSpec.EXACTLY) {
            mWidth = mWidthSize;
        } else {
            //计算宽度,可以根据实际情况进行计算
            mWidth = (getPaddingLeft() + getPaddingRight());
            //如果为AT_MOST, 不允许超过默认宽度的大小
            if (mWidthModle == MeasureSpec.AT_MOST) {
                mWidth = Math.min(mWidth, mWidthSize);
            }
        }
        if (mHeightModle == MeasureSpec.EXACTLY) {
            mHeight = mHeightSize;
        } else {
            mHeight = (getPaddingTop() + getPaddingBottom());
            if (mHeightModle == MeasureSpec.AT_MOST) {
                mHeight = Math.min(mHeight, mHeightSize);
            }
        }
        //设置测量完成的宽高
        setMeasuredDimension(mWidth, mHeight);
        Log.d(TAG, "===========onMeasure: ===========");

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        float barWidth = Math.min(mProgress, mAnimator.getPhaseX()) * mWidth;
        RectF rectF = new RectF();
        rectF.left = 0;
        rectF.right = barWidth;
        rectF.bottom = mHeight;
        rectF.top = 0;
        canvas.drawRect(rectF, mPaint);
    }


    /**
     * 接受0-1之间
     * @param progress
     */
    public void setProgress(Float progress) {
        this.mProgress = progress;
        mAnimator.animateX(3000);
    }
}
