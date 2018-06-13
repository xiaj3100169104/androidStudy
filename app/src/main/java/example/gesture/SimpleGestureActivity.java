package example.gesture;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode2Binding;
import com.style.framework.databinding.GestureSimpleTestBinding;

public class SimpleGestureActivity extends BaseActivity {

    GestureSimpleTestBinding bd;
    private GestureDetector mGestureDetector;

    @Override
    public int getLayoutResId() {
        return R.layout.gesture_simple_test;
    }

    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("状态栏为主题配置里的颜色");

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public static final float FLING_MIN_VELOCITY = 2000f;
            public static final float FLING_MIN_DISTANCE = 200f;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                logE(TAG, "distance  " + (e2.getX() - e1.getX()) + "  velocityX  " + velocityX);
                if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    // Fling left
                    logE(TAG, "向左手势");
                } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    // Fling right
                    logE(TAG, "向右手势");
                } else if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                    // Fling up
                    logE(TAG, "向上手势");
                } else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                    // Fling down
                    logE(TAG, "向下手势");
                }
                return true;
            }
        });
        bd.tvContent1.setLongClickable(true);
        bd.tvContent1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                logE(TAG, "onTouch");
                mGestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }
   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }*/
}
