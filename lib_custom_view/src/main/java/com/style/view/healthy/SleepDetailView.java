package com.style.view.healthy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;
import java.util.Locale;

public class SleepDetailView extends View {
    private DetailBean resultBean;

    private TextPaint mPaint;
    private Paint mChartPaint;
    private static final int mAwakeColor = 0xffB8FAD6;
    private static final int mEyesMoveColor = 0xff4AEA89;
    private static final int mLightSleepColor = 0xff45CE7B;
    private static final int mDeepSleepColor = 0xff0BA648;
    private static final int[] mSleepColor = {mDeepSleepColor, mLightSleepColor, mEyesMoveColor, mAwakeColor};
    private int mViewWidth, mViewHeight;
    //折线图两端到x轴线两端的间距
    private final int xAxisPaddingLeft, xAxisPaddingRight;
    //y轴线最大刻度值与顶部内容区边界距离，因为此时的y刻度值只是y标签与矩形的中点，所以需要一些额外的空间
    private int yContentTopOffset;
    private int mTextPadding;//想走标签到x轴线距离，底部矩形实例到下方文字距离，以及下方文字到下方文字间距
    private final int yLegend2xAxisDistance;
    private int mAxisWidth, mAxisHeight;
    //睡眠端矩形高度，圆角大小
    private int mRectHeight, mRectRadius;
    //x轴线下半部分内容区的高度
    private int mXAxisBottomContentHeight;
    private String[] mAxisYLabels;
    //底部矩形实例宽高
    private int mLegendRectWidth, mLegendRectHeight;
    private int yLabelWidth;
    private int yLabelTextSize = 13;
    private float yLabelHeight;
    private static final int mLegendTextColor = 0xff666666;
    private static final int mXAxisLineColor = 0xff808080;
    private float mLineWidth = 0;

    public SleepDetailView(Context context) {
        this(context, null);
    }

    public SleepDetailView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SleepDetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        yContentTopOffset = dp2px(10);
        xAxisPaddingLeft = dp2px(10);
        xAxisPaddingRight = dp2px(10);
        mTextPadding = dp2px(5);
        yLegend2xAxisDistance = dp2px(10);
        mLegendRectWidth = dp2px(25);
        mLegendRectHeight = dp2px(8);
        mRectHeight = dp2px(8);
        mRectRadius = dp2px(2.7f);
        yLabelTextSize = sp2px(yLabelTextSize);
        mLineWidth = dp2px(0.5f);
        initResources();
    }

    private void initResources() {
        if (isChinese()) {
            mAxisYLabels = new String[]{"深睡", "浅睡", "REM", "清醒"};
        } else {
            mAxisYLabels = new String[]{"Deep", "Light", "REM", "Awake"};
        }
        mChartPaint = new Paint();
        mChartPaint.setAntiAlias(true);
        mChartPaint.setAntiAlias(true);

        mPaint = new TextPaint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(yLabelTextSize);
        yLabelWidth = (int) mPaint.measureText(mAxisYLabels[0]);
        yLabelHeight = getTextHeight(mPaint.getFontMetrics());
        mXAxisBottomContentHeight = (int) (mTextPadding * 3 + yLabelHeight * 3 + yLegend2xAxisDistance + mLegendRectHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mViewWidth, mViewHeight);
        mAxisWidth = mViewWidth - getPaddingLeft() - getPaddingRight() - yLabelWidth - xAxisPaddingLeft - xAxisPaddingRight;
        mAxisHeight = mViewHeight - getPaddingTop() - getPaddingBottom() - yContentTopOffset - mXAxisBottomContentHeight;
        calculateData();
    }

    //activity不处于onresume时，调用刷新不会执行ondraw，相反处于onresume时才会去刷新view
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画睡眠图例
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop() + yContentTopOffset + mAxisHeight + mTextPadding + yLabelHeight + yLegend2xAxisDistance);
        String legendName = null, legendPercent = null, legendTime = null;
        mChartPaint.setShader(null);
        for (int i = 0; i < 4; i++) {
            mChartPaint.setColor(mSleepColor[i]);
            int legendRectXCenter = (mViewWidth - getPaddingLeft() - getPaddingRight()) / 8 * (i * 2 + 1);
            canvas.drawRect(legendRectXCenter - mLegendRectWidth / 2, 0, legendRectXCenter + mLegendRectWidth / 2, mLegendRectHeight, mChartPaint);

            mPaint.setColor(mLegendTextColor);
            legendName = mAxisYLabels[i];
            /*if (resultBean != null) {
                legendPercent = String.valueOf(resultBean.mSleepTime[i] * 60 * 100.0f / resultBean.totalSleepTimeLength);
                legendName = legendName + " " + legendPercent + "%";
            }*/
            int baseLine = (int) (mLegendRectHeight + mTextPadding + Math.abs(mPaint.getFontMetrics().top));
            canvas.drawText(legendName, legendRectXCenter, baseLine, mPaint);
            //跳过画时间文本
            if (resultBean == null) {
                continue;
            }
            legendTime = getTimeStr(resultBean.mSleepTime[i]);
            int baseLine2 = (int) (mLegendRectHeight + mTextPadding + yLabelHeight + mTextPadding + Math.abs(mPaint.getFontMetrics().top));
            canvas.drawText(legendTime, legendRectXCenter, baseLine2, mPaint);
        }
        canvas.restore();
        //画x轴线
        canvas.save();
        canvas.translate(getPaddingLeft() + yLabelWidth + xAxisPaddingLeft, getPaddingTop() + yContentTopOffset + mAxisHeight);
        mChartPaint.setColor(mXAxisLineColor);
        mChartPaint.setStrokeWidth(mLineWidth);
        canvas.drawLine(-xAxisPaddingLeft, 0, mAxisWidth + xAxisPaddingRight, 0, mChartPaint);
        //画x轴线刻度及标签
        if (resultBean != null) {
            int scaleWidth = mAxisWidth / 6;
            for (int i = 0; i < 7; i++) {
                int x = i * scaleWidth;
                canvas.drawLine(x, 0, x, dp2px(5.0f), mChartPaint);
                if (i % 2 == 0) {
                    canvas.drawText(resultBean.xLabel[i], x, mTextPadding + Math.abs(mPaint.getFontMetrics().top), mPaint);
                }
            }
        }
        //画y标签
        for (int i = 0; i < mAxisYLabels.length; i++) {
            int baseLine = (int) -(mAxisHeight / 4 * (i + 1) - getBaseLine2CenterY(mPaint.getFontMetrics()));
            canvas.drawText(mAxisYLabels[i], -xAxisPaddingLeft - yLabelWidth / 2, baseLine, mPaint);
        }
        //画睡眠图
        if (resultBean == null || resultBean.sleepItems == null) {
            canvas.restore();
            return;
        }
        SleepItem nowItem, nextItem;
        for (int i = 0; i < resultBean.sleepItems.size(); i++) {
            //画圆角矩形
            mChartPaint.setStyle(Paint.Style.FILL);
            mChartPaint.setShader(null);
            nowItem = resultBean.sleepItems.get(i);
            mChartPaint.setColor(mSleepColor[nowItem.phaseType]);
            canvas.drawRoundRect(nowItem.mRectF, mRectRadius, mRectRadius, mChartPaint);
            //画连接线
            mPaint.setStrokeWidth(mLineWidth);
            LinearGradient linearGradient;
            int[] colors = null;
            if (i + 1 <= resultBean.sleepItems.size() - 1) {
                nextItem = resultBean.sleepItems.get(i + 1);
                int nowType = nowItem.phaseType;
                int nextType = nextItem.phaseType;
                colors = new int[Math.abs(nextType - nowType) + 1];
                int index = 0;
                if (nextType > nowType) {
                    for (int j = nowType; j <= nextType; j++) {
                        colors[index] = mSleepColor[j];
                        index++;
                    }
                } else {
                    for (int j = nowType; j >= nextType; j--) {
                        colors[index] = mSleepColor[j];
                        index++;
                    }
                }
                //shader颜色渐变数组长度必须大于或等于2
                if (colors.length >= 2)
                    linearGradient = new LinearGradient(nowItem.mRectF.right, nowItem.mRectF.centerY(), nextItem.mRectF.left, nextItem.mRectF.centerY(), colors, null, Shader.TileMode.MIRROR);
                else
                    linearGradient = null;
                mChartPaint.setShader(linearGradient);
                canvas.drawLine(nowItem.mRectF.right - 1, nowItem.mRectF.centerY(), nextItem.mRectF.left + 1, nextItem.mRectF.centerY(), mChartPaint);
            }
        }
        canvas.restore();
    }

    /**
     * 根据时间分钟数获取显示的字符串
     *
     * @param time
     * @return
     */
    private String getTimeStr(int time) {
        String timeStr = "";
        if (time == 0)
            return "0h 0min";

        if (time < 60) {
            timeStr = String.valueOf(time) + "min";
        } else if (time >= 60) {
            timeStr = String.valueOf(time / 60) + "h " + String.valueOf(time % 60) + "min";
        }
        return timeStr;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    private int dp2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 判断语言环境
     *
     * @return
     */
    private boolean isChinese() {
        String language = Locale.getDefault().getLanguage();
        if ("zh".equals(language)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置睡眠数据
     *
     * @param resultBean
     */
    public void setData(DetailBean resultBean) {
        this.resultBean = resultBean;
        requestLayout();
        invalidate();
    }

    /**
     * 需要在测量后再计算数据
     */
    public void calculateData() {
        if (resultBean == null || resultBean.sleepItems == null) {
            return;
        }
        int offset = 0;
        float timeScale = (mAxisWidth + 0.0f) / resultBean.totalSleepTimeLength;
        int itemLength;
        RectF rectF;
        for (SleepItem item : resultBean.sleepItems) {
            rectF = new RectF();
            rectF.left = offset - 1;
            itemLength = (int) (timeScale * item.phaseLength);
            rectF.right = offset + itemLength + 1;
            offset += itemLength;
            rectF.top = -((mAxisHeight * (item.phaseType + 1) + 0.0f) / 4 + mRectHeight / 2);
            rectF.bottom = rectF.top + mRectHeight;
            item.mRectF = rectF;
        }
    }

    /**
     * 计算文字基线到y方向中轴线的距离。
     * 适用于文字y方向中轴线需要与其他刻度对齐时使用。
     *
     * @param fontMetrics
     * @return 基线和centerY的距离
     */
    public static float getBaseLine2CenterY(Paint.FontMetrics fontMetrics) {
        return (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
    }

    public float getTextHeight(Paint.FontMetrics fontMetrics) {
        return Math.abs(fontMetrics.bottom - fontMetrics.top);
    }

    public static class DetailBean {
        //睡眠类型数
        public static int SLEEP_TYPE_COUNT = 4;
        //x坐标刻度数
        public static int X_LABEL_COUNT = 7;
        //深睡到清醒睡眠时间(分钟)
        public int[] mSleepTime;
        //总睡眠时长
        public int totalSleepTimeLength;
        //时间均分刻度
        public String[] xLabel;
        public List<SleepItem> sleepItems;
    }

    public static class SleepItem {
        public int phaseType;
        public int phaseLength;
        public long mStartTime;
        public long mEndTime;
        public RectF mRectF;
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }
    /**
     * MeasureSpec其实就是承担这种作用：MeasureSpec是父控件提供给子View的一个参数，作为设定自身大小参考，只是个参考，要多大，还是View自己说了算。
     * 先看下MeasureSpec的构成，MeasureSpec由size和mode组成，mode包括三种，UNSPECIFIED、EXACTLY、AT_MOST，size就是配合mode给出的参考尺寸，
     * 具体意义如下：
     *
     * UNSPECIFIED(未指定),父控件对子控件不加任何束缚，子元素可以得到任意想要的大小，这种MeasureSpec一般是由父控件自身的特性决定的。
     * 比如ScrollView，它的子View可以随意设置大小，无论多高，都能滚动显示，这个时候，size一般就没什么意义。
     *
     * EXACTLY(完全)，父控件为子View指定确切大小，希望子View完全按照自己给定尺寸来处理，跟上面的场景1跟2比较相似，
     * 这时的MeasureSpec一般是父控件根据自身的MeasureSpec跟子View的布局参数来确定的。一般这种情况下size>0,有个确定值。
     *
     * AT_MOST(至多)，父控件为子元素指定最大参考尺寸，希望子View的尺寸不要超过这个尺寸，跟上面场景3比较相似。
     * 这种模式也是父控件根据自身的MeasureSpec跟子View的布局参数来确定的，一般是子View的布局参数采用wrap_content的时候。
     */
}
