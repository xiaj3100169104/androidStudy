package example.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.style.base.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySecondBinding;


public class AnimatorActivity extends BaseDefaultTitleBarActivity {


    private ActivitySecondBinding bd;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_second;
    }


    @Override
    public void initData() {
        bd = getBinding();
        bd.btnValueAnimator.setOnClickListener(v -> valueAnimator());
        bd.btnObjectAnimator.setOnClickListener(v -> objectAnimator());
        bd.btnAnimatorSet.setOnClickListener(v -> animatorSet());
        bd.btnAnimatorXml.setOnClickListener(v -> animatorFromXml());

    }

    public void valueAnimator() {
        float fromX = bd.ivProperty.getTranslationX();
        ValueAnimator va = ValueAnimator.ofFloat(fromX, fromX + 100f, fromX);
        va.setDuration(1000);
        //插值器，表示值变化的规律，默认均匀变化
        va.setInterpolator(new BounceInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                bd.ivProperty.setTranslationX(v);
                Log.d(getTAG(), "v:" + v);
            }
        });
        va.start();
        ObjectAnimator oa = new ObjectAnimator();
    }

    public void objectAnimator() {
        float fromX = bd.ivProperty.getTranslationX();
        ObjectAnimator oa = ObjectAnimator.ofFloat(bd.ivProperty, "translationX", fromX, fromX + 100f, fromX);
        oa.setDuration(300);
        oa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        oa.start();

    }

    public void animatorSet() {
        ObjectAnimator oaScaleX = ObjectAnimator.ofFloat(bd.ivProperty, "scaleX", 0, 1);
        ObjectAnimator oaScaleY = ObjectAnimator.ofFloat(bd.ivProperty, "scaleY", 0, 1);
        ObjectAnimator oaRotation = ObjectAnimator.ofFloat(bd.ivProperty, "rotation", 0, 360);
        ObjectAnimator oaAlpha = ObjectAnimator.ofFloat(bd.ivProperty, "alpha", 1, 0);
        AnimatorSet as = new AnimatorSet();
        as.play(oaScaleX).with(oaScaleY).with(oaRotation);
        as.play(oaAlpha).after(oaRotation);
        as.setDuration(1000);
        as.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                bd.ivProperty.setAlpha(1.0f);
                Log.d(getTAG(), "finish:" + bd.ivProperty.getAlpha());
            }
        });
        as.start();
    }

    public void animatorFromXml() {
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.property_demo);
        animator.setTarget(bd.ivProperty);
        animator.start();

    }

  /*AccelerateDecelerateInterpolator 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
  AccelerateInterpolator  在动画开始的地方速率改变比较慢，然后开始加速
  AnticipateInterpolator 开始的时候向后然后向前甩
  AnticipateOvershootInterpolator 开始的时候向后然后向前甩一定值后返回最后的值
  BounceInterpolator   动画结束的时候弹起
  CycleInterpolator 动画循环播放特定的次数，速率改变沿着正弦曲线
  DecelerateInterpolator 在动画开始的地方快然后慢
  LinearInterpolator   以常量速率改变
  OvershootInterpolator    向前甩一定值后再回到原来位置*/

}
