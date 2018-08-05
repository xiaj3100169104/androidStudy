package example.gesture;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.dmcbig.mediapicker.utils.ScreenUtils;
import com.style.base.BaseActivity;
import com.style.framework.R;

/**
 * 左滑销毁activity基类，操作体验更好
 * Created by xiajun on 2018/8/1.
 * 注意：主题窗口背景设置半透明，去掉activity退出动画
 */

public abstract class BaseLeftSlideFinishActivity extends BaseActivity {

    protected final String TAG = getClass().getSimpleName();
    private int screenWidth;
    private ViewConfiguration viewConfiguration;
    private float xDown;
    private float yDown;
    private boolean isCanSlide;
    private boolean isStartSlide;
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
                isCanSlide = true;
                isStartSlide = false;
                xDown = ev.getRawX();
                yDown = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isCanSlide) {
                    float xMove;
                    if (!isStartSlide) {
                        xMove = ev.getRawX();
                        float yMove = ev.getRawY();
                        float xDistance = Math.abs(xMove - xDown);
                        float yDistance = Math.abs(yMove - yDown);
                        //一旦向左滑便认为不触发左滑行为。
                        if (xMove < xDown) {
                            isCanSlide = false;
                        }
                        //当横向滑动超过指定值并且横向滑动距离大于竖向滑动距离时，拦截move、up事件，触发ViewGroup横向滑动逻辑
                        if (xMove - xDown > viewConfiguration.getScaledTouchSlop() && xDistance > yDistance) {
                            isStartSlide = true;
                            Log.e(TAG, "start move");
                            xLastMove = xMove;
                            return true;
                        }
                    } else {//处理当前容器滑动逻辑
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
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isCanSlide && isStartSlide) {
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
