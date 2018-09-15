package example.gesture;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by xiajun on 2018/9/14.
 */

public class CustomHorizontalSlideView extends FrameLayout {
    protected final String TAG = getClass().getSimpleName();

    private boolean mIsBeingDragged = false;
    boolean isScrollFinished = true;
    /**
     * Position of the last motion event.
     */
    private int mLastMotionX;
    private int mTouchSlop;

    public CustomHorizontalSlideView(@NonNull Context context) {
        super(context);
        initD(context);
    }


    public CustomHorizontalSlideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initD(context);

    }

    public CustomHorizontalSlideView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initD(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomHorizontalSlideView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initD(context);

    }

    private void initD(Context context) {
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            //recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        Log.e(TAG, "onInterceptTouchEvent   " + action);
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }

       /* if (super.onInterceptTouchEvent(ev)) {
            return true;
        }*/

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {

                final int x = (int) ev.getX();
                final int xDiff = (int) Math.abs(x - mLastMotionX);
                if (xDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                    mLastMotionX = x;
                    if (getParent() != null) getParent().requestDisallowInterceptTouchEvent(true);
                    Log.e(TAG, "start move");
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                final int x = (int) ev.getX();
                if (!inChild((int) x, (int) ev.getY())) {
                    mIsBeingDragged = false;
                    break;
                }
                mLastMotionX = x;
                /*
                * If being flinged and user touches the screen, initiate drag;
                * otherwise don't.  mScroller.isFinished should be false when
                * being flinged.
                */
                mIsBeingDragged = !isScrollFinished;
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                /* Release the drag */
                mIsBeingDragged = false;
                break;
        }

        /*
        * The only time we want to intercept motion events is if we are in the
        * drag mode.
        */
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        Log.e(TAG, "onTouchEvent   " + action);

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                if (getChildCount() == 0) {
                    return false;
                }
                if ((mIsBeingDragged = !isScrollFinished)) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }

                /*
                 * If being flinged and user touches, stop the fling. isFinished
                 * will be false if being flinged.
                 */
                if (!isScrollFinished) {

                }

                // Remember where the motion event started
                mLastMotionX = (int) ev.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE:
                final int x = (int) ev.getX();
                int deltaX = mLastMotionX - x;
                if (!mIsBeingDragged && Math.abs(deltaX) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
                    // Scroll to follow the motion event
                    Log.i(TAG, "translationX  " + deltaX);

                    mLastMotionX = x;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    Log.e(TAG, "stop move");
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged && getChildCount() > 0) {

                    mIsBeingDragged = false;

                }
                break;
        }
        return true;
    }

    private boolean inChild(int x, int y) {
        if (getChildCount() > 0) {
            final int scrollX = 0;
            final View child = getChildAt(0);
            return !(y < child.getTop()
                    || y >= child.getBottom()
                    || x < child.getLeft() - scrollX
                    || x >= child.getRight() - scrollX);
        }
        return false;
    }

    /**
     * 处理外接设备滚动事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return super.onGenericMotionEvent(event);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return true;
    }
}
