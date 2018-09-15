package example.drag;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.dmcbig.mediapicker.utils.ScreenUtils;
import com.style.utils.Utils;

/**
 * 右滑销毁activity的根容器，用于子view有scrollView的时候，操作体验更便捷
 * Created by xiajun on 2018/8/1.
 * 注意：如果用在activity销毁时，主题窗口背景设置半透明，去掉activity退出动画
 */

public class SwipeMenuView extends RelativeLayout {
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
    private int screenWidth;
    //滑动开始与点击事件的位移临界值
    private int mTouchSlop;
    private int currentSlideState = SLIDE_STATE_IDLE;

    protected final String TAG = getClass().getSimpleName();
    private Scroller scroller;
    //最大偏移，正值代表向右，负值代表向左
    private int xMaxOffset;
    private ViewConfiguration viewConfiguration;
    private float xDown;
    private float yDown;
    private float xDistance;
    private float yDistance;
    private float xLastMove;
    private ValueAnimator va;
    private OnSlideListener listener;
    private boolean isMenuOpen;
    private boolean isCanSlideToRight;

    public SwipeMenuView(Context context) {
        super(context);
    }

    public SwipeMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        screenWidth = ScreenUtils.getScreenWidth(context);
        xMaxOffset = -Utils.dp2px(context, 200);
        scroller = new Scroller(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent   " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getRawX();
                yDown = ev.getRawY();
                //如果菜单已经打开并且再次按下在左边内容区
                if (isMenuOpen && (xDown >= 0 && xDown <= screenWidth + xMaxOffset)) {
                    isCanSlideToRight = true;
                }
                return false;
            //break;
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
                //菜单处于关闭状态，
                if (!isMenuOpen && currentSlideState == SLIDE_STATE_LEFT_FIRST && (xMove - xDown < -mTouchSlop && xDistance > yDistance)) {
                    currentSlideState = SLIDE_STATE_MOVE_WITH_TOUCH;
                    Log.e(TAG, "start move");
                    xLastMove = xMove;
                    if (getParent() != null)
                        getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                //菜单处于打开状态时，不管什么方向都拦截，避免列表上下滑动
                if (isMenuOpen && isCanSlideToRight && (xDistance > mTouchSlop || yDistance > mTouchSlop)) {
                    currentSlideState = SLIDE_STATE_MOVE_WITH_TOUCH;
                    xLastMove = xMove;
                    if (getParent() != null)
                        getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                break;
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
                float xMove = ev.getRawX();
                if (!isMenuOpen && currentSlideState == SLIDE_STATE_MOVE_WITH_TOUCH) {
                    float translationX = 0 + xMove - xLastMove;
                    //yDistance = Math.abs(yMove - yDown);
                    Log.i(TAG, "translationX  " + translationX);
                    //Log.e(TAG, "yDistance  " + yDistance);
                    if (translationX <= 0 && translationX >= xMaxOffset) {
                        setTranslationX(translationX);
                        //xLastMove = xMove;
                    }
                }
                if (isMenuOpen && isCanSlideToRight && currentSlideState == SLIDE_STATE_MOVE_WITH_TOUCH) {
                    float translationX = xMaxOffset + xMove - xLastMove;
                    //yDistance = Math.abs(yMove - yDown);
                    Log.i(TAG, "translationX  " + translationX);
                    //Log.e(TAG, "yDistance  " + yDistance);
                    if (translationX <= 0 && translationX >= xMaxOffset) {
                        setTranslationX(translationX);
                        //xLastMove = xMove;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentSlideState == SLIDE_STATE_MOVE_WITH_TOUCH) {
                    Log.e(TAG, "stop move");
                    autoTranslationX();
                }
                break;
        }
        return true;
    }

    private void autoTranslationX() {
        float xOffset = getTranslationX();
        if (xOffset <= xMaxOffset / 2)
            va = ValueAnimator.ofFloat(xOffset, xMaxOffset);
        if (xOffset < 0 && xOffset > xMaxOffset / 2)
            va = ValueAnimator.ofFloat(xOffset, 0f);

        va.setDuration(300);
        //插值器，表示值变化的规律，默认均匀变化
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(animation -> {
            float v = (float) animation.getAnimatedValue();
            Log.d(TAG, "translationX:" + v);
            setTranslationX(v);

        });
        va.start();
    }

    @Override
    public void setTranslationX(float translationX) {
        super.setTranslationX(translationX);
        if (translationX <= xMaxOffset) {
            isMenuOpen = true;
            currentSlideState = SLIDE_STATE_IDLE;
            isCanSlideToRight = false;
        }
        if (translationX >= 0) {
            currentSlideState = SLIDE_STATE_IDLE;
            isMenuOpen = false;
            isCanSlideToRight = false;
        }
    }

    private void onViewSlideToScreen() {
        if (listener != null)
            listener.onShouldFinish();
    }

    public void setOnSlideListener(OnSlideListener listener) {
        this.listener = listener;
    }

    public interface OnSlideListener {
        void onShouldFinish();

    }
}
