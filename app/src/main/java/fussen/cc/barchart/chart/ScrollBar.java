package fussen.cc.barchart.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.style.framework.ChartAnimator;

import static fussen.cc.barchart.chart.ScrollBar.Direction.RIGHT;


/**
 * Created by Fussen on 2017/4/21.
 *
 * 可以滚动的条形图
 */
public class ScrollBar extends View {

    private static final String TAG = "ScrollBar";
    private Paint mChartPaint;
    private Rect mBound;

    private float textStart;
    private int mHeight;
    private int mWidth;

    private List<Float> verticalList = new ArrayList<>();
    private List<String> horizontalList = new ArrayList<>();

    private float verticalWidth = 100f;
    private float chartWidth; //表的总宽度，除过外间距
    private float outSpace = verticalWidth;// 柱子与纵轴的距离
    private float startChart = verticalWidth; //柱子开始的横坐标

    private float interval;//柱子之间的间隔
    private float barWidth;//柱子的宽度

    private float bottomHeight = 100f;//底部横坐标高度

    private String maxValue = "2";//默认最大值
    private String middleValue = "1";

    private int paddingTop = 20;

    private Paint noDataPaint;
    private TextPaint textXpaint;
    private Paint linePaint;

    private String noDataColor = "#66FF6933";
    private String textColor = "#FFBEBEBE";
    private String lineColor = "#E4E5E6";
    private String chartColor = "#FF6933";
    private String yBgColor = "#66FF6933";
    private int mDuriation = 3000;

    private TextPaint textYpaint;

    private Context mContext;

    private ChartAnimator mAnimator;
    private GestureDetectorCompat mGestureDetector;
    private OverScroller mScroller;
    private Paint yBackgroundPaint;

    public enum Direction {
        NONE, LEFT, RIGHT, VERTICAL
    }

    //正常滑动方向
    private Direction mCurrentScrollDirection = Direction.NONE;
    //快速滑动方向
    private Direction mCurrentFlingDirection = Direction.NONE;

    private boolean mHorizontalFlingEnabled = true;

    private PointF mCurrentOrigin = new PointF(0f, 0f);

    private int mScrollDuration = 250;

    //滑动速度
    private float mXScrollingSpeed = 1f;
    private int mMinimumFlingVelocity = 0;


    ///////////////////////////////////////////////////////////////////////////
    // 滑动相关
    ///////////////////////////////////////////////////////////////////////////


    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        //手指按下
        @Override
        public boolean onDown(MotionEvent e) {
            goToNearestBar();
            return true;
        }

        //有效的滑动
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


            switch (mCurrentScrollDirection) {
                case NONE:
                    // 只允许在一个方向上滑动
                    if (Math.abs(distanceX) > Math.abs(distanceY)) {
                        if (distanceX > 0) {
                            mCurrentScrollDirection = Direction.LEFT;
                        } else {
                            mCurrentScrollDirection = RIGHT;
                        }
                    } else {
                        mCurrentScrollDirection = Direction.VERTICAL;
                    }
                    break;
                case LEFT:
                    // Change direction if there was enough change.
                    if (Math.abs(distanceX) > Math.abs(distanceY) && (distanceX < 0)) {
                        mCurrentScrollDirection = RIGHT;
                    }
                    break;
                case RIGHT:
                    // Change direction if there was enough change.
                    if (Math.abs(distanceX) > Math.abs(distanceY) && (distanceX > 0)) {
                        mCurrentScrollDirection = Direction.LEFT;
                    }
                    break;
            }


            // 重新计算滑动后的起点
            switch (mCurrentScrollDirection) {
                case LEFT:
                case RIGHT:
                    mCurrentOrigin.x -= distanceX * mXScrollingSpeed;
                    ViewCompat.postInvalidateOnAnimation(ScrollBar.this);
                    break;
            }

            return true;
        }

        //快速滑动
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


            if ((mCurrentFlingDirection == Direction.LEFT && !mHorizontalFlingEnabled) ||
                    (mCurrentFlingDirection == RIGHT && !mHorizontalFlingEnabled)) {
                return true;
            }

            mCurrentFlingDirection = mCurrentScrollDirection;

            mScroller.forceFinished(true);

            switch (mCurrentFlingDirection) {
                case LEFT:
                case RIGHT:
                    mScroller.fling((int) mCurrentOrigin.x, (int) mCurrentOrigin.y, (int) (velocityX * mXScrollingSpeed), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
                    break;
                case VERTICAL:
                    break;
            }

            ViewCompat.postInvalidateOnAnimation(ScrollBar.this);
            return true;
        }


        //单击事件
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        //长按
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }
    };


    @Override
    public void computeScroll() {
        super.computeScroll();


        if (mScroller.isFinished()) {//当前滚动是否结束
            if (mCurrentFlingDirection != Direction.NONE) {
                goToNearestBar();
            }
        } else {
            if (mCurrentFlingDirection != Direction.NONE && forceFinishScroll()) { //惯性滑动时保证最左边条目展示正确
                goToNearestBar();
            } else if (mScroller.computeScrollOffset()) {//滑动是否结束 记录最新的滑动的点 惯性滑动处理
                mCurrentOrigin.y = mScroller.getCurrY();
                mCurrentOrigin.x = mScroller.getCurrX();
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

    }


    /**
     * Check if scrolling should be stopped.
     *
     * @return true if scrolling should be stopped before reaching the end of animation.
     */
    private boolean forceFinishScroll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return mScroller.getCurrVelocity() <= mMinimumFlingVelocity;
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将view的OnTouchEvent事件交给手势监听器处理
        boolean val = mGestureDetector.onTouchEvent(event);

        // 正常滑动结束后 处理最左边的条目
        if (event.getAction() == MotionEvent.ACTION_UP && mCurrentFlingDirection == Direction.NONE) {
            if (mCurrentScrollDirection == RIGHT || mCurrentScrollDirection == Direction.LEFT) {
                goToNearestBar();
            }
            mCurrentScrollDirection = Direction.NONE;
        }
        return val;
    }

    private void goToNearestBar() {

        //让最左边的条目 显示出来
        double leftBar = mCurrentOrigin.x / (barWidth + interval);

        if (mCurrentFlingDirection != Direction.NONE) {
            // 跳到最近一个bar
            leftBar = Math.round(leftBar);
        } else if (mCurrentScrollDirection == Direction.LEFT) {
            // 跳到上一个bar
            leftBar = Math.floor(leftBar);
        } else if (mCurrentScrollDirection == RIGHT) {
            // 跳到下一个bar
            leftBar = Math.ceil(leftBar);
        } else {
            // 跳到最近一个bar
            leftBar = Math.round(leftBar);
        }

        int nearestOrigin = (int) (mCurrentOrigin.x - leftBar * (barWidth + interval));

        if (nearestOrigin != 0) {
            // 停止当前动画
            mScroller.forceFinished(true);
            //开始滚动
            mScroller.startScroll((int) mCurrentOrigin.x, (int) mCurrentOrigin.y, -nearestOrigin, 0, (int) (Math.abs(nearestOrigin) / (barWidth + interval) * mScrollDuration));
            ViewCompat.postInvalidateOnAnimation(ScrollBar.this);
        }
        //重新设置滚动方向.
        mCurrentScrollDirection = mCurrentFlingDirection = Direction.NONE;

    }

    public ScrollBar(Context context) {
        this(context, null);
        this.mContext = context;
        init();
    }

    public ScrollBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
        init();
    }

    public ScrollBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        init();
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

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight() - paddingTop;

        chartWidth = mWidth - outSpace;
        //每个柱子宽度
        barWidth = 100f;
        interval = 30f;

        //柱子开始的横坐标
        startChart = outSpace;

        //////////////////////////////////////////

        //横坐标

        textStart = startChart + (barWidth / 2f);

    }

    private void init() {

        //初始化手势

        mGestureDetector = new GestureDetectorCompat(mContext, mGestureListener);

        // 解决长按屏幕后无法拖动的现象 但是 长按 用不了
        mGestureDetector.setIsLongpressEnabled(false);


        mScroller = new OverScroller(mContext, new FastOutLinearInInterpolator());


        mMinimumFlingVelocity = ViewConfiguration.get(mContext).getScaledMinimumFlingVelocity();


        //初始化动画
        mAnimator = new ChartAnimator(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
            }
        });

        mBound = new Rect();


        //柱子画笔
        mChartPaint = new Paint();
        mChartPaint.setAntiAlias(true);
        mChartPaint.setColor(Color.parseColor(chartColor));


        //线画笔
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor(lineColor));

        //x纵坐标 画笔
        textXpaint = new TextPaint();
        textXpaint.setAntiAlias(true);
        textXpaint.setTextSize(27f);
        textXpaint.setTextAlign(Paint.Align.CENTER);
        textXpaint.setColor(Color.parseColor(textColor));

        //Y纵坐标 画笔
        textYpaint = new TextPaint();
        textYpaint.setAntiAlias(true);
        textYpaint.setTextSize(28f);
        textYpaint.setTextAlign(Paint.Align.LEFT);
        textYpaint.setColor(Color.parseColor(textColor));

        //Y轴背景
        yBackgroundPaint = new Paint();
        yBackgroundPaint.setColor(Color.parseColor(yBgColor));

        //无数据时的画笔
        noDataPaint = new Paint();
        noDataPaint.setAntiAlias(true);
        noDataPaint.setColor(Color.parseColor(noDataColor));
        noDataPaint.setStyle(Paint.Style.FILL);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        float lineInterval = (mHeight - bottomHeight) / 4f; //横线之间的间距  纵向
        float textHeight = mHeight + paddingTop - bottomHeight;//横坐标高度

        //控制图表滑动左右边界
        if (mCurrentOrigin.x < getWidth() - (verticalList.size() * barWidth + (verticalList.size() - 1) * interval + outSpace))
            mCurrentOrigin.x = getWidth() - (verticalList.size() * barWidth + (verticalList.size() - 1) * interval + outSpace);

        if (mCurrentOrigin.x > 0)
            mCurrentOrigin.x = 0;


        //画线
        drawLine(canvas, lineInterval, textHeight);

        //画纵坐标
        drawYtext(canvas, lineInterval, textHeight);

        //画横坐标
        float textTempStart = textStart;

        drawXtext(canvas, textTempStart);

        float chartTempStart = startChart;

        float size = (mHeight - bottomHeight) / 100f; //比例

        //画柱子
        drawBar(canvas, chartTempStart, size);

    }

    /**
     * 画柱子
     *
     * @param canvas
     * @param chartTempStart
     * @param size
     */
    private void drawBar(Canvas canvas, float chartTempStart, float size) {


        canvas.clipRect(outSpace - 10f, 0, mWidth, getHeight(), Region.Op.REPLACE);

        for (int i = 0; i < verticalList.size(); i++) {

            //每个数据点所占的Y轴高度
            float barHeight = verticalList.get(i) / Float.valueOf(maxValue) * 100f * size;

            float realBarHeight = barHeight * mAnimator.getPhaseY();

            //画柱状图 矩形
            RectF rectF = new RectF();
            rectF.left = chartTempStart + mCurrentOrigin.x;
            rectF.top = (mHeight - bottomHeight + paddingTop) - realBarHeight;
            rectF.right = chartTempStart + barWidth + mCurrentOrigin.x;
            rectF.bottom = mHeight + paddingTop - bottomHeight;

            canvas.drawRect(rectF, mChartPaint);

            chartTempStart += (barWidth + interval);
        }
    }

    /**
     * 画x轴
     *
     * @param canvas
     * @param textTempStart
     */
    private void drawXtext(Canvas canvas, float textTempStart) {

        canvas.clipRect(outSpace, getHeight() - bottomHeight, mWidth, getHeight(), Region.Op.REPLACE);

        for (int i = 0; i < verticalList.size(); i++) {
            textXpaint.getTextBounds(horizontalList.get(i), 0, horizontalList.get(i).length(), mBound);
            canvas.drawText(horizontalList.get(i), textTempStart + mCurrentOrigin.x, mHeight + paddingTop - 60f + mBound.height() / 2f, textXpaint);
//                canvas.drawLine(textTempStart, mHeight + paddingTop - bottomHeight, textTempStart, 0, linePaint);
            textTempStart += (barWidth + interval);
        }
    }

    /**
     * 画Y轴
     *
     * @param canvas
     * @param lineInterval
     * @param textHeight
     */
    private void drawYtext(Canvas canvas, float lineInterval, float textHeight) {


        canvas.clipRect(0, 0, outSpace, mHeight, Region.Op.REPLACE);

        canvas.drawText("0", 0f, textHeight, textYpaint);

        canvas.drawText(middleValue, 0f, textHeight - lineInterval * 2f + 10f, textYpaint);
        canvas.drawText(maxValue, 0f, textHeight - lineInterval * 4f + 10f, textYpaint);
    }

    /**
     * 画线
     *
     * @param canvas
     * @param lineInterval
     * @param textHeight
     */
    private void drawLine(Canvas canvas, float lineInterval, float textHeight) {
        canvas.drawLine(outSpace - 10f, textHeight, mWidth, textHeight, linePaint);
        canvas.drawLine(outSpace - 10f, textHeight - lineInterval, mWidth, textHeight - lineInterval, linePaint);
        canvas.drawLine(outSpace - 10f, textHeight - lineInterval * 2f, mWidth, textHeight - lineInterval * 2f, linePaint);
        canvas.drawLine(outSpace - 10f, textHeight - lineInterval * 3f, mWidth, textHeight - lineInterval * 3f, linePaint);
        canvas.drawLine(outSpace - 10f, textHeight - lineInterval * 4f, mWidth, textHeight - lineInterval * 4f, linePaint);
    }


    /**
     * 重新指定起始位置
     *
     * @param verticalList
     */
    private void measureWidthShort(List<Float> verticalList) {
        startChart = outSpace;
        textStart = startChart + barWidth / 2f;
    }


    /**
     * 设置纵轴数据
     *
     * @param verticalList
     */
    public void setVerticalList(List<Float> verticalList) {

        if (verticalList != null) {
            this.verticalList = verticalList;

        } else {
            maxValue = "2";
            middleValue = "1";
            invalidate();
            return;
        }


        measureWidthShort(verticalList);

        if (Collections.max(verticalList) > 2) {
            int tempMax = Math.round(Collections.max(verticalList));
            while (tempMax % 10 != 0) {
                tempMax++;
            }
            int middle = tempMax / 2;
            maxValue = String.valueOf(tempMax);
            middleValue = String.valueOf(middle);
        } else {
            maxValue = "2";
            middleValue = "1";
        }

        mAnimator.animateY(mDuriation);
    }

    /**
     * 设置横轴数据
     *
     * @param horizontalList
     */
    public void setHorizontalList(List<String> horizontalList) {
        if (horizontalList != null)
            this.horizontalList = horizontalList;
    }

}