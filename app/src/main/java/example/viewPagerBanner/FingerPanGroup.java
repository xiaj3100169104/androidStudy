package example.viewPagerBanner;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.github.chrisbanes.photoview.PhotoView;
import com.style.framework.R;

/**
 * Created by sdj on 2018/1/18.
 */

public class FingerPanGroup extends LinearLayout {
    private static final String TAG = FingerPanGroup.class.getSimpleName();

    private PhotoView view3;

    private float mDownY;
    private float mTranslationY;
    private float mLastTranslationY;
    private static int MAX_TRANSLATE_Y = 500;
    private final static int MAX_EXIT_Y = 300;
    private final static long DURATION = 150;
    private boolean isAnimate = false;
    private int fadeIn = R.anim.fade_in;
    private int fadeOut = R.anim.fade_out;
    private int mTouchslop;
    private onAlphaChangedListener mOnAlphaChangedListener;
    private float xDown;
    private float xMove;
    private float yMove;


    public FingerPanGroup(Context context) {
        this(context, null);
    }

    public FingerPanGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FingerPanGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        mTouchslop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view3 = (PhotoView) getChildAt(0);
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
                if (null != view3) {
                    xMove = ev.getRawX();
                    yMove = ev.getRawY();
                    float xDistance = Math.abs(xMove - xDown);
                    float yDistance = Math.abs(yMove - mDownY);
                    //当横向滑动超过指定值并且横向滑动距离大于竖向滑动距离时，拦截move、up事件，触发ViewGroup横向滑动逻辑
                    isIntercept = ev.getPointerCount() == 1 && view3.getScale() <= view3.getMinimumScale() && yDistance > mTouchslop && yDistance > xDistance;
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
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getRawY();
            case MotionEvent.ACTION_MOVE:
                if (null != view3) {
                    onOneFingerPanActionMove(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                onActionUp();
                break;
        }
        return true;
    }

    private void onOneFingerPanActionMove(MotionEvent event) {
        float moveY = event.getRawY();
        mTranslationY = moveY - mDownY + mLastTranslationY;
        float percent = Math.abs(mTranslationY / (MAX_TRANSLATE_Y + view3.getHeight()));
        float mAlpha = (1 - percent);
        if (mAlpha > 1) {
            mAlpha = 1;
        } else if (mAlpha < 0) {
            mAlpha = 0;
        }
        ViewGroup linearLayout = (ViewGroup) getParent();
        if (null != linearLayout) {
            linearLayout.getBackground().mutate().setAlpha((int) (mAlpha * 255));
        }
        //触发回调 根据距离处理其他控件的透明度 显示或者隐藏角标，文字信息等
        if (null != mOnAlphaChangedListener) {
            mOnAlphaChangedListener.onTranslationYChanged(mTranslationY);
        }
        //ViewHelper.setScrollY(this, -(int) mTranslationY);
    }

    private void onActionUp() {
        if (Math.abs(mTranslationY) > MAX_EXIT_Y) {
            exitWithTranslation(mTranslationY);
        } else {
            resetCallBackAnimation();
        }
    }

    public void exitWithTranslation(float currentY) {
        if (currentY > 0) {
            ValueAnimator animDown = ValueAnimator.ofFloat(mTranslationY, getHeight());
            animDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = (float) animation.getAnimatedValue();
                    //ViewHelper.setScrollY(FingerPanGroup.this, -(int) fraction);
                }
            });
            animDown.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    reset();
                    Activity activity = ((Activity) getContext());
                    activity.finish();
                    activity.overridePendingTransition(fadeIn, fadeOut);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animDown.setDuration(DURATION);
            animDown.setInterpolator(new LinearInterpolator());
            animDown.start();
        } else {
            ValueAnimator animUp = ValueAnimator.ofFloat(mTranslationY, -getHeight());
            animUp.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = (float) animation.getAnimatedValue();
                    //ViewHelper.setScrollY(FingerPanGroup.this, -(int) fraction);
                }
            });
            animUp.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    reset();
                    ((Activity) getContext()).finish();
                    ((Activity) getContext()).overridePendingTransition(fadeIn, fadeOut);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animUp.setDuration(DURATION);
            animUp.setInterpolator(new LinearInterpolator());
            animUp.start();
        }
    }

    private void resetCallBackAnimation() {
        ValueAnimator animatorY = ValueAnimator.ofFloat(mTranslationY, 0);
        animatorY.setDuration(DURATION);
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (isAnimate) {
                    mTranslationY = (float) valueAnimator.getAnimatedValue();
                    mLastTranslationY = mTranslationY;
                    //ViewHelper.setScrollY(FingerPanGroup.this, -(int) mTranslationY);
                }
            }
        });
        animatorY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isAnimate) {
                    mTranslationY = 0;
                    ViewGroup linearLayout = (ViewGroup) getParent();
                    if (null != linearLayout) {
                        linearLayout.getBackground().mutate().setAlpha(255);
                    }
                    invalidate();
                    reset();
                }
                isAnimate = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorY.start();
    }


    public interface onAlphaChangedListener {
        void onAlphaChanged(float alpha);

        void onTranslationYChanged(float translationY);
    }

    //暴露的回调方法（可根据位移距离或者alpha来改变主UI控件的透明度等
    public void setOnAlphaChangeListener(onAlphaChangedListener alphaChangeListener) {
        mOnAlphaChangedListener = alphaChangeListener;
    }

    private void reset() {
        if (null != mOnAlphaChangedListener) {
            mOnAlphaChangedListener.onTranslationYChanged(mTranslationY);
            mOnAlphaChangedListener.onAlphaChanged(1);
        }
    }
}
