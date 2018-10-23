package example.gesture;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dmcbig.mediapicker.utils.ScreenUtils;
import com.style.utils.Utils;

/**
 * 垂直弹性滑动View
 */

public class XXRefreshView extends LinearLayout {

    protected final String TAG = getClass().getSimpleName();
    private View header;
    //下拉触发刷新临界点
    private int mRefreshPaddingTopSlop;
    private int screenWidth;
    //滑动开始与点击事件的位移临界值
    private int mTouchSlop;
    //最大下拉偏移
    private int maxOffset;
    private ViewConfiguration viewConfiguration;
    private boolean mIsBeingDragged;
    private float yLastMove;
    private ValueAnimator va;
    private OnSlideListener listener;
    private float xDown;
    private float yDown;

    public XXRefreshView(Context context) {
        super(context);
    }

    public XXRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        screenWidth = ScreenUtils.getScreenWidth(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        header = getChildAt(0);
        maxOffset = -header.getLayoutParams().height;
        mRefreshPaddingTopSlop = maxOffset / 2;
        setPaddingTop(maxOffset);
    }

    public void setPaddingTop(float paddingTop) {
        if (paddingTop <= 0 && paddingTop >= maxOffset)
            setPadding(0, (int) paddingTop, 0, 0);
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
                xDown = ev.getRawX();
                yDown = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float xMove = ev.getRawX();
                float yMove = ev.getRawY();
                float xDistance = Math.abs(xMove - xDown);
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
                xDown = ev.getRawX();
                yDown = ev.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float xMove = ev.getRawX();
                float yMove = ev.getRawY();
                if (!mIsBeingDragged) {
                    float xDistance = Math.abs(xMove - xDown);
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
                    float yDistance = Math.abs(yMove - yLastMove) + maxOffset;
                    Log.e(TAG, "yDistance  " + yDistance);
                    setPaddingTop(yDistance);
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

    public void setHeaderHeight(float h) {
        if (h >= maxOffset) {
            //this.header.getLayoutParams().height = (int) h;
            //this.header.requestLayout();
            setPaddingTop(h);
        }
    }

    private void autoMove() {
        float xOffset = getPaddingTop();
        if (xOffset <= mRefreshPaddingTopSlop && xOffset >= maxOffset) {
            va = ValueAnimator.ofFloat(xOffset, maxOffset);
            va.setDuration(300);
            //插值器，表示值变化的规律，默认均匀变化
            va.setInterpolator(new DecelerateInterpolator());
            va.addUpdateListener(animation -> {
                float v = (float) animation.getAnimatedValue();
                Log.d(TAG, "translationY:" + v);
                setPaddingTop(v);
            });
            va.start();
        } else {
            va = ValueAnimator.ofFloat(xOffset, mRefreshPaddingTopSlop);
            va.setDuration(100);
            //插值器，表示值变化的规律，默认均匀变化
            va.setInterpolator(new DecelerateInterpolator());
            va.addUpdateListener(animation -> {
                float v = (float) animation.getAnimatedValue();
                Log.d(TAG, "translationY:" + v);
                setPaddingTop(v);
                startRefresh();
            });
            va.start();
        }
    }

    private void startRefresh() {
        postDelayed(() -> {
            finishRefresh();
        }, 2000);
    }

    private void finishRefresh() {
        float xOffset = getPaddingTop();
        va = ValueAnimator.ofFloat(xOffset, maxOffset);
        va.setDuration(100);
        //插值器，表示值变化的规律，默认均匀变化
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(animation -> {
            float v = (float) animation.getAnimatedValue();
            Log.d(TAG, "translationY:" + v);
            setPaddingTop(v);
        });
        va.start();
    }

    private void onViewSlideToScreen() {
        if (listener != null)
            listener.onShouldFinish();
    }

    public void setOnSlideListener(OnSlideListener listener) {
        this.listener = listener;
    }

    public boolean isCanPullDown() {
        return getChildAt(1).canScrollVertically(-1);
    }


    public interface OnSlideListener {
        void onShouldFinish();
    }

}
