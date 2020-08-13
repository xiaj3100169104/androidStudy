package com.style.view.other

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import androidx.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

class RecordAudioView2 : View {
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    //振幅矩形宽度
    private var mHistogramWidth: Int
    //振幅矩形圆角大小
    private var mHistogramCorner: Float
    //振幅矩形最小高度
    private var mHistogramMinHeight: Int
    //振幅矩形最大高度
    private var mHistogramMaxHeight: Int
    //单边矩形个数
    private val mHistogramCount = 20
    //振幅矩形间隔宽度
    private var mHistogramIntervalWidth: Int
    //中间空隙宽度
    private var mMiddleIntervalWidth: Int
    private lateinit var mHistogramPaint: Paint
    private val COLOR_HISTOGRAM = 0xFFFF5E00
    private var mData = arrayListOf<Float>()
    private var mRect = RectF()

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mHistogramWidth = dp2px(2.0f)
        mHistogramCorner = dp2px(1.0f).toFloat()
        mHistogramMinHeight = dp2px(2.0f)
        mHistogramMaxHeight = dp2px(48.0f)
        mHistogramIntervalWidth = dp2px(3.0f)
        mMiddleIntervalWidth = dp2px(3.0f)
        for (i in 0 until mHistogramCount)
            mData.add(0.0f)
        initPaint()
    }

    private fun initPaint() {
        mHistogramPaint = Paint()
        mHistogramPaint.isAntiAlias = true
        mHistogramPaint.color = COLOR_HISTOGRAM.toInt()
        mHistogramPaint.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mWidth = paddingLeft + paddingRight + mHistogramWidth * mHistogramCount * 2 + mHistogramIntervalWidth * (mHistogramCount - 1) * 2 + mMiddleIntervalWidth
        mHeight = paddingBottom + paddingTop + mHistogramMaxHeight
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.save()
        canvas?.translate((mWidth / 2).toFloat(), (mHeight / 2).toFloat())
        mData.forEachIndexed { index, value ->
            val left: Float = (mMiddleIntervalWidth / 2 + (mHistogramWidth + mHistogramIntervalWidth) * index).toFloat()
            val right = left + mHistogramWidth
            var bottom = mHistogramMaxHeight / 2 * value
            bottom = if (bottom < mHistogramMinHeight / 2) (mHistogramMinHeight / 2).toFloat() else bottom
            val top = -bottom
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //右端
                canvas?.drawRoundRect(left, top, right, bottom, mHistogramCorner, mHistogramCorner, mHistogramPaint)
                //左端
                canvas?.drawRoundRect(-right, top, -left, bottom, mHistogramCorner, mHistogramCorner, mHistogramPaint)
            } else {
                mRect.set(left, top, right, bottom)
                canvas?.drawRoundRect(mRect, mHistogramCorner, mHistogramCorner, mHistogramPaint)
                mRect.set(-right, top, -left, bottom)
                canvas?.drawRoundRect(mRect, mHistogramCorner, mHistogramCorner, mHistogramPaint)
            }
        }
        canvas?.restore()
    }

    fun postValue(v: Float) {
        mData.add(v)
        mData.removeAt(0)
        postInvalidate()
    }

    fun reset() {
        for (i in mData.indices) {
            mData[i] = 0.0f
        }
        postInvalidate()
    }

    private fun measureWidth(measureSpec: Int): Int {
        var result = 0
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        when (specMode) {
            View.MeasureSpec.UNSPECIFIED -> result = specSize
            View.MeasureSpec.AT_MOST -> result = Math.min(result, specSize)
            View.MeasureSpec.EXACTLY -> result = specSize
        }
        return result
    }

    private fun measureHeight(measureSpec: Int): Int {
        var result = 0
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        when (specMode) {
            View.MeasureSpec.UNSPECIFIED -> result = specSize
            View.MeasureSpec.AT_MOST -> result = Math.min(result, specSize)
            View.MeasureSpec.EXACTLY -> result = specSize
        }
        return result
    }

    private fun dp2px(dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}