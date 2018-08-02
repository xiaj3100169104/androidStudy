package example.gesture;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by xiajun on 2018/6/16.
 */

public class DispatchGestureFromScroll extends RelativeLayout {
    protected final String TAG = getClass().getSimpleName();
    private ViewConfiguration viewConfiguration;
    private float xDown;
    private float yDown;
    private float xMove;
    private float yMove;
    private boolean isStartSlide;
    private float xDistance;
    private float yDistance;

    public DispatchGestureFromScroll(Context context) {
        super(context);
    }

    public DispatchGestureFromScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewConfiguration = ViewConfiguration.get(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent   " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isStartSlide = false;
                xDown = ev.getX();
                yDown = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isStartSlide) {
                    xMove = ev.getX();
                    yMove = ev.getY();
                    float xDistance = Math.abs(xMove - xDown);
                    float yDistance = Math.abs(yMove - yDown);
                    //当横向滑动超过指定值并且横向滑动距离大于竖向滑动距离时，拦截move、up事件，触发ViewGroup横向滑动逻辑
                    if (xMove - xDown > viewConfiguration.getScaledTouchSlop() && xDistance > yDistance) {
                        isStartSlide = true;
                        Log.e(TAG, "start move");
                        return true;
                    }
                } else {//处理当前容器滑动逻辑
                    return true;
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
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = ev.getX();
                yMove = ev.getY();

                xDistance = xMove - xDown;
                //yDistance = Math.abs(yMove - yDown);
                Log.e(TAG, "xDistance  " + xDistance);
                //Log.e(TAG, "yDistance  " + yDistance);
                moveLayout(xDistance);
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "stop move");
                break;

        }
        return true;
        //return super.onTouchEvent(ev);
    }

    private void moveLayout(float xDistance) {
        scrollTo((int) -xDistance, 0);
    }
}
