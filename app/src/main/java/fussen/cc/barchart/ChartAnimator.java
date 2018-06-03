package com.style.framework;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

/**
 * Created by Fussen on 2017/4/21.
 */

public class ChartAnimator {


    protected float mPhaseY = 1f; //0f-1f
    protected float mPhaseX = 1f; //0f-1f

    private ValueAnimator.AnimatorUpdateListener mListener;

    public ChartAnimator(ValueAnimator.AnimatorUpdateListener listener) {
        mListener = listener;
    }

    /**
     * Y轴动画
     *
     * @param durationMillis
     */
    public void animateY(int durationMillis) {

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setDuration(durationMillis);
        animatorY.addUpdateListener(mListener);
        animatorY.start();
    }


    /**
     * X轴动画
     *
     * @param durationMillis
     */
    public void animateX(int durationMillis) {

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorY.setDuration(durationMillis);
        animatorY.addUpdateListener(mListener);
        animatorY.start();
    }


    /**
     * This gets the y-phase that is used to animate the values.
     *
     * @return
     */
    public float getPhaseY() {
        return mPhaseY;
    }

    /**
     * This modifys the y-phase that is used to animate the values.
     *
     * @param phase
     */
    public void setPhaseY(float phase) {
        mPhaseY = phase;
    }


    /**
     * This modifys the X-phase that is used to animate the values.
     *
     * @param phase
     */
    public void setPhaseX(float phase) {
        mPhaseX = phase;
    }


    /**
     * This gets the x-phase that is used to animate the values.
     *
     * @return
     */
    public float getPhaseX() {
        return mPhaseX;
    }

}
