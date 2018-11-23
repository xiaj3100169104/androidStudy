package com.xiajun.xxrefreshview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.xiajun.xxrefreshview.header.XXRefreshDefaultHeader;

/**
 * 自定义刷新View
 */

public class XXRefreshView extends LinearLayout {
    public static final int STATE_HEADER_IDLE = 0;
    public static final int STATE_HEADER_PULL_DOWN_NOT_CAN_REFRESH = 1;
    public static final int STATE_HEADER_PULL_DOWN_RELEASE_CAN_REFRESH = 2;
    public static final int STATE_HEADER_REFRESH = 3;
    private int state = STATE_HEADER_IDLE;

    protected final String TAG = getClass().getSimpleName();
    private XXRefreshHeader header;
    //下拉触发刷新高度
    private int mRefreshSlop;
    //滑动开始与点击事件的位移临界值
    private int mTouchSlop;
    //最大下拉距离
    private int maxOffset;
    private ViewConfiguration viewConfiguration;
    private boolean mIsBeingDragged;
    private int yLastMove;
    private ValueAnimator va;
    private OnRefreshListener listener;
    private int xDown;
    private int yDown;

    public XXRefreshView(Context context) {
        super(context);
    }

    public XXRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        maxOffset = Utils.dp2px(context, 300);
        mRefreshSlop = Utils.dp2px(context, 80);
        XXRefreshDefaultHeader header = new XXRefreshDefaultHeader(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, maxOffset);
        addView(header, 0, params);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View child = getChildAt(0);
        setPaddingTop(-maxOffset);
        header = (XXRefreshHeader) child;
        header.initHeight(mRefreshSlop, maxOffset);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 如果子view拦截了down事件，后续事件就会走onInterceptTouchEvent。
     * 如果子view没有拦截down事件，后续事件就不会再走onInterceptTouchEvent，而走onTouchEvent，如果onTouchEvent拦截了down事件，后续事件便传给onTouchEvent。
     * 如果onTouchEvent不拦截down事件，后续事件都不会传给该容器了。
     * 因此需要在两个方法分别处理拖拽行为。
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent   " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) ev.getRawX();
                yDown = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int xMove = (int) ev.getRawX();
                int yMove = (int) ev.getRawY();
                int xDistance = Math.abs(xMove - xDown);
                int yDistance = Math.abs(yMove - yDown);
                //当横向滑动超过指定值时，拦截move、up事件，触发ViewGroup横向滑动逻辑
                if (yMove - yDown > mTouchSlop && yDistance > xDistance) {
                    if (!isCanPullDown()) {
                        mIsBeingDragged = true;
                        Log.e(TAG, "start move");
                        yLastMove = yMove;
                        return true;
                    }
                }
                break;
            //ACTION_MOVE被当前view或者子view消耗了这里就收不到ACTION_UP事件
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onTouchEvent   " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) ev.getRawX();
                yDown = (int) ev.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                int xMove = (int) ev.getRawX();
                int yMove = (int) ev.getRawY();
                if (!mIsBeingDragged) {
                    int xDistance = Math.abs(xMove - xDown);
                    float yDistance = Math.abs(yMove - yDown);
                    //当横向滑动超过指定值时，拦截move、up事件，触发ViewGroup横向滑动逻辑
                    if (yMove - yDown > mTouchSlop && yDistance > xDistance) {
                        if (!isCanPullDown()) {
                            mIsBeingDragged = true;
                            Log.e(TAG, "start move");
                            yLastMove = yMove;
                            return true;
                        }
                    }
                }
                if (mIsBeingDragged) {
                    //防止上滑越界，并能滑至上临界点
                    if (yMove < yLastMove) {
                        yMove = yLastMove;
                    }
                    //拖拽距离除以2有阻力效果
                    int yDistance = Math.abs(yMove - yLastMove) / 2;
                    Log.e(TAG, "pullDownDistance  " + yDistance);
                    beingDrag(yDistance);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "stop move");
                mIsBeingDragged = false;
                autoMove();
                break;

        }
        return true;
    }

    public void beingDrag(int yDistance) {
        int paddingTop = pullDownDistance2paddingTop(yDistance);
        setPaddingTop(paddingTop);
        if (yDistance <= mRefreshSlop)
            setState(STATE_HEADER_PULL_DOWN_NOT_CAN_REFRESH);
        if (yDistance > mRefreshSlop)
            setState(STATE_HEADER_PULL_DOWN_RELEASE_CAN_REFRESH);
    }

    private void autoMove() {
        //下拉距离
        int pullDownDistance = getPullDownDistance();
        //小于临界值直接关闭header
        if (pullDownDistance <= mRefreshSlop) {
            closeHeader();
        } else {
            //超过了临界值，回滚到临界值
            int paddingTop = getPaddingTop();
            //触发刷新临界点的paddingTop值
            int mRefreshSlopPaddingTop = pullDownDistance2paddingTop(mRefreshSlop);
            va = ValueAnimator.ofInt(paddingTop, mRefreshSlopPaddingTop);
            va.setDuration(getAnimatorTimeByDistance(Math.abs(mRefreshSlopPaddingTop) - Math.abs(paddingTop)));
            //插值器，表示值变化的规律，默认均匀变化
            va.setInterpolator(new DecelerateInterpolator());
            va.addUpdateListener(animation -> {
                int v = (int) animation.getAnimatedValue();
                Log.d(TAG, "paddingTop:" + v);
                setPaddingTop(v);
                //回滚到临界值时触发刷新
                if (v == mRefreshSlopPaddingTop) {
                    setState(STATE_HEADER_REFRESH);
                    startRefresh();
                }
            });
            va.start();
        }
    }

    private void startRefresh() {
        postDelayed(() -> {
            finishRefresh();
        }, 2000);
    }

    public void finishRefresh() {
        closeHeader();
    }

    //关闭header，此时header可见高度小于触发刷新的临界值
    private void closeHeader() {
        int paddingTop = getPaddingTop();
        va = ValueAnimator.ofInt(paddingTop, -maxOffset);
        va.setDuration(getAnimatorTimeByDistance(maxOffset - Math.abs(paddingTop)));
        //插值器，表示值变化的规律，默认均匀变化
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(animation -> {
            int v = (int) animation.getAnimatedValue();
            Log.d(TAG, "paddingTop:" + v);
            setPaddingTop(v);
            setState(STATE_HEADER_PULL_DOWN_NOT_CAN_REFRESH);
        });
        va.start();
    }

    public int pullDownDistance2paddingTop(int distance) {
        return -(maxOffset - Math.abs(distance));
    }

    public int getPullDownDistance() {
        return maxOffset - Math.abs(getPaddingTop());
    }

    //默认一百像素一百毫秒
    public int getAnimatorTimeByDistance(int distance) {
        return Math.abs(distance);
    }

    public boolean isCanPullDown() {
        return getChildAt(1).canScrollVertically(-1);
    }

    public void setPaddingTop(int paddingTop) {
        if (paddingTop <= 0 && paddingTop >= -maxOffset) {
            setPadding(0, paddingTop, 0, 0);
            if (header != null) {
                header.onVisibleHeightChanged(getPullDownDistance());
            }
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        if (header != null)
            header.onState(this.state);
    }

    private void onRefresh() {
        if (listener != null)
            listener.onRefresh();
    }

    public void setOnSlideListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

}
