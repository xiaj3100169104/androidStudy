package example.gesture;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.GestureSimpleTestBinding;

public class SimpleGestureActivity extends BaseActivity {

    GestureSimpleTestBinding bd;
    private GestureDetector mGestureDetector;

    @Override
    public int getLayoutResId() {
        return R.layout.gesture_simple_test;
    }


    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("区分上下、左右滑动");

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public static final float FLING_MIN_VELOCITY = 2000f;
            public static final float FLING_MIN_DISTANCE = 200f;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                logE(TAG, "distance  " + (e2.getX() - e1.getX()) + "  velocityX  " + velocityX);
                if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    logE(TAG, "向左手势");
                } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    logE(TAG, "向右手势");
                } else if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                    logE(TAG, "向上手势");
                } else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                    logE(TAG, "向下手势");
                }
                return false;
            }
        });
        bd.tvContent1.setLongClickable(true);
        /*bd.tvContent1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                logE(TAG, "onTouch   " + event.getAction());
                bd.nestedScrollView.requestDisallowInterceptTouchEvent(true);
                mGestureDetector.onTouchEvent(event);
                return false;
            }
        });*/
        bd.tvContent1.setOnTouchListener(new MyGestureListener(bd.nestedScrollView){
            @Override
            public void onSlideLeft() {
                super.onSlideLeft();
            }

            @Override
            public void onSlideRight() {
                super.onSlideRight();
            }
        });
    }
   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }*/
}
