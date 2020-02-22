package example.gesture;

import android.support.v4.widget.NestedScrollView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import static com.style.utils.LogManager.logE;

/**
 * Created by xiajun on 2018/6/18.
 */

public class MyGestureListener implements View.OnTouchListener {
    protected final String TAG = getClass().getSimpleName();
    private final NestedScrollView nestedScrollView;
    private GestureDetector mGestureDetector;

    public MyGestureListener(NestedScrollView nestedScrollView) {
        this.nestedScrollView = nestedScrollView;
        mGestureDetector = new GestureDetector(nestedScrollView.getContext(), new SimpleSlideOnGestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        logE(TAG, "onTouch   " + event.getAction());
        if (nestedScrollView != null) {
            this.nestedScrollView.requestDisallowInterceptTouchEvent(true);
        }
        mGestureDetector.onTouchEvent(event);
        return false;
    }

    class SimpleSlideOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        public static final float FLING_MIN_VELOCITY = 2000f;
        public static final float FLING_MIN_DISTANCE = 200f;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            logE(TAG, "distance  " + (e2.getX() - e1.getX()) + "  velocityX  " + velocityX);
            if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                logE(TAG, "向左手势");
                onSlideLeft();
            } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                logE(TAG, "向右手势");
                onSlideRight();
            } else if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                logE(TAG, "向上手势");
            } else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                logE(TAG, "向下手势");
            }
            return false;
        }
    }

    public void onSlideLeft() {

    }

    public void onSlideRight() {

    }
}
