package example.viewPagerBanner;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import com.style.data.app.AppManager;
import com.style.base.activity.BaseFullScreenStableActivity;

import org.jetbrains.annotations.Nullable;

public abstract class BaseVerticalSlideFinishActivity extends BaseFullScreenStableActivity {

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

        viewConfiguration = ViewConfiguration.get(getContext());
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

    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

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
                    getRootView().setTranslationY(translationY);
                    onTranslationY(translationY);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isStartSlide) {
                    yUp = ev.getRawY();
                    ValueAnimator va = ValueAnimator.ofFloat(getRootView().getTranslationY(), 0f);
                    va.setDuration(300);
                    //插值器，表示值变化的规律，默认均匀变化
                    va.setInterpolator(new DecelerateInterpolator());
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float v = (float) animation.getAnimatedValue();
                            Log.d(getTAG(), "translationY:" + v);
                            getRootView().setTranslationY(v);
                            onTranslationY(v);
                        }
                    });
                    va.start();
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //从不透明到完全透明需要拖动的距离
    int maxD = 240 * 3;
    ColorDrawable windowBackgroundDrawable = new ColorDrawable();

    private void onTranslationY(float translationY) {
        int a = 255;
        int distance = (int) Math.abs(translationY);
        a = (int) (255 - (distance * 255 + 0.0f) / maxD);
        a = a > 255 ? 255 : a;
        a = a < 0 ? 0 : a;
        Log.e("Alpha", "Alpha   " + a);
        //getRootView().getBackground().setAlpha(a);
        setWindowAlpha(a);
    }

    protected void setWindowAlpha(int alpha) {
        int c = Color.argb(alpha, 0, 0, 0);
        windowBackgroundDrawable.setColor(c);
        getWindow().setBackgroundDrawable(windowBackgroundDrawable);
    }

    //只要子view有任何点击或触摸事件便不会执行此方法。所以需慎用。
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e(getTAG(), "onTouchEvent   " + ev.getAction());
        return super.onTouchEvent(ev);
    }

}