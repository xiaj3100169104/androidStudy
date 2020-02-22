package example.viewPagerBanner;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.github.chrisbanes.photoview.PhotoView;
import com.style.utils.DeviceInfoUtil;

/**
 * 处理viewPager左右滑动浏览图片时可以上下滑动finish activity.
 * Created by Style on 2018/12/29.
 */

public class FingerPanGroup extends LinearLayout {
    private static final String TAG = FingerPanGroup.class.getSimpleName();
    //背景由不透明渐变到透明需要拖拽的距离
    private final int MAX_ALPHA_Y_OFFSET;
    //退出activity偏移临界值，手指抬起时超过这个值就退出activity
    private final int MAX_EXIT_Y_OFFSET;
    private final static long DURATION = 150;
    private final int mTouchSlop;

    private boolean isAnimate = false;
    private FingerPanGroup mContainer;
    private PhotoView mPhotoView;
    private onAlphaChangedListener mOnAlphaChangedListener;

    private float mDownY;
    private float xDown;
    private float yLastMove;


    public FingerPanGroup(Context context) {
        this(context, null);
    }

    public FingerPanGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FingerPanGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        MAX_ALPHA_Y_OFFSET = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, context.getResources().getDisplayMetrics());
        MAX_EXIT_Y_OFFSET = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContainer = this;
        mPhotoView = (PhotoView) getChildAt(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        int action = ev.getAction() & ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getRawX();
                mDownY = ev.getRawY();
            case MotionEvent.ACTION_MOVE:
                if (null != mPhotoView) {
                    float xMove = ev.getRawX();
                    float yMove = ev.getRawY();
                    float xDistance = Math.abs(xMove - xDown);
                    float yDistance = Math.abs(yMove - mDownY);
                    //当横向滑动超过指定值并且横向滑动距离大于竖向滑动距离时，拦截move、up事件，触发ViewGroup横向滑动逻辑
                    isIntercept = ev.getPointerCount() == 1 && mPhotoView.getScale() <= mPhotoView.getMinimumScale() && yDistance > mTouchSlop && yDistance > xDistance;
                    yLastMove = yMove;
                }
                break;
        }
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent   " + event.getAction());
        int action = event.getAction() & event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                float yMove = event.getRawY();
                float translationY = yMove - yLastMove;
                setPhotoViewTranslationY(translationY);
                break;
            case MotionEvent.ACTION_UP:
                onActionUp();
                break;
        }
        return true;
    }

    private void setPhotoViewTranslationY(float translationY) {
        Log.e(TAG, "translationY  " + translationY);
        mPhotoView.setTranslationY(translationY);
        int a = 255;
        int distance = (int) Math.abs(translationY);
        a = (int) (255 - (distance * 255 + 0.0f) / MAX_ALPHA_Y_OFFSET);
        a = a > 255 ? 255 : a;
        a = a < 0 ? 0 : a;
        Log.e("Alpha", "Alpha   " + a);
        setWindowAlpha(a);
        //触发回调 根据距离处理其他控件的透明度 显示或者隐藏角标，文字信息等
        if (null != mOnAlphaChangedListener) {
            mOnAlphaChangedListener.onTranslationYChanged(translationY);
            mOnAlphaChangedListener.onAlphaChanged(a);
        }
    }

    protected void setWindowAlpha(int alpha) {
        mContainer.getBackground().mutate().setAlpha(alpha);
    }

    private void onActionUp() {
        float mTranslationY = mPhotoView.getTranslationY();
        if (Math.abs(mTranslationY) > MAX_EXIT_Y_OFFSET) {
            exitWithTranslation(mTranslationY);
        } else {
            resetCallBackAnimation(mTranslationY);
        }
    }

    public void exitWithTranslation(float mTranslationY) {
        ValueAnimator animExit;
        //向下移动并离开
        if (mTranslationY > 0) {
            animExit = ValueAnimator.ofFloat(mTranslationY, getHeight());
        } else {//向上移动并离开
            animExit = ValueAnimator.ofFloat(mTranslationY, -getHeight());
        }
        animExit.setDuration(DURATION);
        animExit.setInterpolator(new LinearInterpolator());
        animExit.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                setPhotoViewTranslationY(v);
            }
        });
        animExit.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Activity activity = ((Activity) getContext());
                activity.finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animExit.start();
    }

    private void resetCallBackAnimation(float mTranslationY) {
        ValueAnimator animBack = ValueAnimator.ofFloat(mTranslationY, 0);
        animBack.setDuration(DURATION);
        animBack.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                setPhotoViewTranslationY(v);
            }
        });
        animBack.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimate = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animBack.start();
    }

    //暴露的回调方法（可根据位移距离或者alpha来改变主UI控件的透明度等
    public void setOnAlphaChangeListener(onAlphaChangedListener alphaChangeListener) {
        mOnAlphaChangedListener = alphaChangeListener;
    }

    public interface onAlphaChangedListener {
        void onAlphaChanged(float alpha);

        void onTranslationYChanged(float translationY);
    }
}
