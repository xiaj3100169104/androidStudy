package example.gesture;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import com.style.app.AppManager;
import com.style.base.activity.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityTestGestureBinding;

import org.jetbrains.annotations.Nullable;

public class VerticalSlideFinishActivity extends BaseActivity {

    ActivityTestGestureBinding bd;
    private float xDown;
    private float yDown;
    private boolean isShouldFinish;
    private ViewConfiguration viewConfiguration;
    private View rootView;
    private boolean isStartSlide;
    private float xMove;
    private float yMove;
    private float yLastMove;
    private float yUp;

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        return;
    }

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_test_gesture);
        bd = getBinding();
        rootView = bd.getRoot();
        viewConfiguration = ViewConfiguration.get(getContext());

        bd.iv.setOnClickListener(v -> {
            logE(getTAG(), "iv");
        });
        AppManager.Companion.getInstance().setTestTaskId(getTaskId());
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.Companion.getInstance().setTestTaskId(-1);
    }

    //除非是顶级拖拽事件（不管ziview处于睡眠状态都拦截），否则不予轻易在此方法中拦截事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(getTAG(), "dispatchTouchEvent   " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isStartSlide = false;
                xDown = ev.getRawX();
                yDown = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isStartSlide) {
                    xMove = ev.getRawX();
                    yMove = ev.getRawY();
                    float xDistance = Math.abs(xMove - xDown);
                    float yDistance = Math.abs(yMove - yDown);
                    //当横向滑动超过指定值并且横向滑动距离大于竖向滑动距离时，拦截move、up事件，触发ViewGroup横向滑动逻辑
                    if (yDistance > viewConfiguration.getScaledTouchSlop() && yDistance > xDistance) {
                        isStartSlide = true;
                        Log.e(getTAG(), "start move");
                        yLastMove = yMove;
                        return true;
                    }
                } else {//处理当前容器滑动逻辑
                    yMove = ev.getRawY();
                    float translationY = yMove - yLastMove;
                    Log.e(getTAG(), "translationY  " + translationY);
                    getRoot().setTranslationY(translationY);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isStartSlide) {
                    yUp = ev.getRawY();
                    ValueAnimator va = ValueAnimator.ofFloat(getRoot().getTranslationY(), 0f);
                    va.setDuration(300);
                    //插值器，表示值变化的规律，默认均匀变化
                    va.setInterpolator(new DecelerateInterpolator());
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float v = (float) animation.getAnimatedValue();
                            getRoot().setTranslationY(v);
                            Log.d(getTAG(), "translationY:" + v);
                        }
                    });
                    va.start();
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    //只要子view有任何点击或触摸事件便不会执行此方法。所以需慎用。
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e(getTAG(), "onTouchEvent   " + ev.getAction());
        return super.onTouchEvent(ev);
    }

    public View getRoot() {
        return rootView;
    }
}