package com.style.view.healthy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EcgView extends View {
    private final String TAG = this.getClass().getSimpleName();
    //拐角处弧度半径
    private float cornerRadius = 10f;
    private int bigHorizontalGridNum = 8;
    private int bigVerticalGridNum = 4;
    private float smallHorizontalGridWidth;
    private float smallVerticalGridHeight;
    private int mLineWidth;

    //控件宽高
    private int mViewWidth, mViewHeight;
    private Paint gridLinePaint, linePaint;

    private int maxY = 100;
    private ArrayList<Integer> dataList = new ArrayList<>();
    private ArrayList<Integer> mIndexList = new ArrayList<>();
    private Path mLinePath;

    //左绘制边界
    private float leftDrawOffset;
    //右绘制边界
    private float rightDrawOffset;

    Thread animationThread;
    private boolean isStart;
    private float mXDataIntervalWidth;
    private int mXOffset = 0;
    private int mMaxOffset;
    private PlayListener mListener;

    public EcgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mLinePath = new Path();
        mLineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5f, getResources().getDisplayMetrics());
        cornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cornerRadius, getResources().getDisplayMetrics());

        gridLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridLinePaint.setStrokeJoin(Paint.Join.ROUND);// 笔刷图形样式
        gridLinePaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔转弯的连接风格
        gridLinePaint.setDither(true);//防抖动
        gridLinePaint.setShader(null);//设置渐变色
        gridLinePaint.setStyle(Paint.Style.FILL);
        gridLinePaint.setColor(Color.parseColor("#FF7770"));

        linePaint = new Paint(gridLinePaint);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(mLineWidth);
        linePaint.setPathEffect(new CornerPathEffect(cornerRadius));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        smallHorizontalGridWidth = mViewWidth / (5f * bigHorizontalGridNum);
        smallVerticalGridHeight = mViewHeight / (5f * bigVerticalGridNum);
        mXDataIntervalWidth = smallHorizontalGridWidth;
        leftDrawOffset = -smallHorizontalGridWidth * 2;
        rightDrawOffset = mViewWidth + smallHorizontalGridWidth * 2;
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGrid(canvas);
        drawLine(canvas);
    }

    private void drawGrid(Canvas canvas) {
        //画横线
        for (int i = 0; i <= 5 * bigVerticalGridNum; i++) {
            if (i % 5 == 0) {
                gridLinePaint.setStrokeWidth(1);
            } else {
                gridLinePaint.setStrokeWidth(0.5f);
            }
            canvas.drawLine(0, i * smallVerticalGridHeight, mViewWidth, i * smallVerticalGridHeight, gridLinePaint);
        }
        //画竖线
        for (int i = 0; i <= 5 * bigHorizontalGridNum; i++) {
            if (i % 5 == 0) {
                gridLinePaint.setStrokeWidth(1);
            } else {
                gridLinePaint.setStrokeWidth(0.5f);
            }
            canvas.drawLine(i * smallHorizontalGridWidth, 0, i * smallHorizontalGridWidth, mViewHeight, gridLinePaint);
        }
    }

    private void drawLine(Canvas canvas) {
        if (dataList.size() <= 1) return;
        if (isStart) {
            mIndexList.clear();
            for (int i = 0; i < dataList.size(); i++) {
                float x = (mViewWidth + mXDataIntervalWidth * i) - mXOffset;
                if (x > leftDrawOffset && x < rightDrawOffset)
                    mIndexList.add(i);
                if (x > rightDrawOffset)
                    break;
            }

            canvas.save();
            canvas.translate(0, mViewHeight / 2);
            mLinePath.reset();
            float x, y;
            for (int i = 0; i < mIndexList.size(); i++) {
                x = (mViewWidth + mXDataIntervalWidth * mIndexList.get(i)) - mXOffset;
                y = -mViewHeight / 2 / 100 * dataList.get(mIndexList.get(i));
                if (i == 0) {
                    mLinePath.moveTo(x, y);
                } else {
                    mLinePath.lineTo(x, y);
                }
            }
            canvas.drawPath(mLinePath, linePaint);
            canvas.restore();
        }
    }

    public void setData(ArrayList<Integer> data) {
        dataList.clear();
        if (data != null) {
            dataList.addAll(data);
        }
        mXOffset = 0;
        mMaxOffset = (int) (mViewWidth + mXDataIntervalWidth * (dataList.size() - 1));
    }

    public void reset() {
        pause();
        isStart = false;
        mXOffset = 0;
        invalidate();
    }

    public void start() {
        if (animationThread != null && animationThread.isAlive()) {
            return;
        }
        Log.e(TAG, "播放开始");
        if (mListener != null)
            mListener.onStarted();
        isStart = true;
        animationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (mXOffset <= mMaxOffset) {
                        mXOffset += 5;
                        postInvalidate();
                        Thread.sleep(30);
                    }
                    //播放完成
                    isStart = false;
                    mXOffset = 0;
                    postInvalidate();
                    Log.e(TAG, "播放结束");
                    if (mListener != null)
                        mListener.onEnd();
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

    public void setListener(PlayListener listener) {
        this.mListener = listener;
    }

    public interface PlayListener {
        void onStarted();

        void onEnd();
    }
}
