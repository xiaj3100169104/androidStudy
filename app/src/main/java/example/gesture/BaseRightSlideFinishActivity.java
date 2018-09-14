package example.gesture;

import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import com.dmcbig.mediapicker.utils.ScreenUtils;
import com.style.base.BaseTitleBarActivity;

/**
 * 右滑销毁activity基类，操作体验更好
 * Created by xiajun on 2018/8/1.
 * 注意：主题窗口背景设置半透明，滑动销毁时要去掉activity退出动画
 */

public abstract class BaseRightSlideFinishActivity extends BaseTitleBarActivity {
    //静止状态
    private static final int SLIDE_STATE_IDLE = 0;
    //第一个move事件为左滑
    private static final int SLIDE_STATE_LEFT_FIRST = 1;
    //第一个move事件为右滑
    private static final int SLIDE_STATE_RIGHT_FIRST = 2;
    //跟随触摸移动
    private static final int SLIDE_STATE_MOVE_WITH_TOUCH = 3;
    //自动向左完成移动
    private static final int SLIDE_STATE_AUTO_TO_LEFT = 4;
    //自动向右完成移动
    private static final int SLIDE_STATE_AUTO_TO_TIGHT = 5;
    private int currentSlideState = SLIDE_STATE_IDLE;

    protected final String TAG = getClass().getSimpleName();
    //最大偏移
    private int screenWidth;
    private ViewConfiguration viewConfiguration;
    private float xDown;
    private float yDown;
    private float xLastMove;
    private ValueAnimator autoMove;
    private boolean isDeleteExitAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewConfiguration = ViewConfiguration.get(this);
        screenWidth = ScreenUtils.getScreenWidth(this);
    }

    @Override
    public void setContentView(View view) {
        view.setBackground(new ColorDrawable(0xFFF5F5F5));
        super.setContentView(view);
    }

    @Override
    protected void onPause() {
        if (isDeleteExitAnim) {
            overridePendingTransition(0, 0);
        }
        super.onPause();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent   " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentSlideState = SLIDE_STATE_IDLE;
                xDown = ev.getRawX();
                yDown = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float xMove = ev.getRawX();
                float yMove = ev.getRawY();
                float xDistance = Math.abs(xMove - xDown);
                float yDistance = Math.abs(yMove - yDown);
                //一旦向左滑便认为不触发滑动行为。
                if (currentSlideState == SLIDE_STATE_IDLE) {
                    if (xMove < xDown) {
                        currentSlideState = SLIDE_STATE_LEFT_FIRST;
                    }
                    if (xMove > xDown) {
                        currentSlideState = SLIDE_STATE_RIGHT_FIRST;
                    }
                }
                if (currentSlideState == SLIDE_STATE_RIGHT_FIRST) {
                    //当横向滑动超过指定值并且横向滑动距离大于竖向滑动距离时，拦截move、up事件，触发ViewGroup横向滑动逻辑
                    if (xMove - xDown > viewConfiguration.getScaledTouchSlop() && xDistance > yDistance) {
                        currentSlideState = SLIDE_STATE_MOVE_WITH_TOUCH;
                        Log.e(TAG, "start move");
                        xLastMove = xMove;
                        return true;
                    }
                }
                //处理当前容器滑动逻辑
                if (currentSlideState == SLIDE_STATE_MOVE_WITH_TOUCH) {
                    xMove = ev.getRawX();
                    float translationX = xMove - xLastMove;
                    //yDistance = Math.abs(yMove - yDown);
                    Log.e(TAG, "translationX  " + translationX);
                    //Log.e(TAG, "yDistance  " + yDistance);
                    //move的x不能小于开始移动的x；不然就会向左滑出屏幕
                    if (xMove >= xLastMove) {
                        getContentView().setTranslationX(translationX);
                        //xLastMove = xMove;
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentSlideState == SLIDE_STATE_MOVE_WITH_TOUCH) {
                    Log.e(TAG, "stop move");
                    autoTranslationX();
                    return true;
                }
                break;

        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onTouchEvent   " + ev.getAction());
        return super.onTouchEvent(ev);
    }

    private void autoTranslationX() {
        float xOffset = getContentView().getTranslationX();
        if (xOffset >= screenWidth / 2) {
            isDeleteExitAnim = true;
            autoMove = ValueAnimator.ofFloat(xOffset, screenWidth);
        }
        if (xOffset > 0 && xOffset < screenWidth / 2)
            autoMove = ValueAnimator.ofFloat(xOffset, 0f);

        autoMove.setDuration(250);
        //插值器，表示值变化的规律，默认均匀变化
        autoMove.setInterpolator(new DecelerateInterpolator());
        autoMove.addUpdateListener(animation -> {
            float v = (float) animation.getAnimatedValue();
            Log.d(TAG, "translationY:" + v);
            getContentView().setTranslationX(v);
            if (v >= screenWidth)
                onViewSlideToScreen();
        });
        autoMove.start();
    }

    private void onViewSlideToScreen() {
        finish();
    }

}
