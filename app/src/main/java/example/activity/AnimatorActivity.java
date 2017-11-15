package example.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;

import butterknife.Bind;
import butterknife.OnClick;

public class AnimatorActivity extends BaseToolBarActivity {

    @Bind(R.id.iv_property)
    ImageView ivProperty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_second;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.btn_value_animator)
    public void skip414() {
        float fromX = ivProperty.getTranslationX();
        ValueAnimator va = ValueAnimator.ofFloat(fromX, fromX + 100f, fromX);
        va.setDuration(1000);
        //插值器，表示值变化的规律，默认均匀变化
        va.setInterpolator(new BounceInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                ivProperty.setTranslationX(v);
                Log.d(TAG, "v:" + v);
            }
        });
        va.start();
        ObjectAnimator oa = new ObjectAnimator();
    }

    @OnClick(R.id.btn_object_animator)
    public void skip415() {
        float fromX = ivProperty.getTranslationX();
        ObjectAnimator oa = ObjectAnimator.ofFloat(ivProperty, "translationX", fromX, fromX + 100f, fromX);
        oa.setDuration(300);
        oa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        oa.start();

    }

    @OnClick(R.id.btn_animator_set)
    public void skip416() {
        ObjectAnimator oaScaleX = ObjectAnimator.ofFloat(ivProperty, "scaleX", 0, 1);
        ObjectAnimator oaScaleY = ObjectAnimator.ofFloat(ivProperty, "scaleY", 0, 1);
        ObjectAnimator oaRotation = ObjectAnimator.ofFloat(ivProperty, "rotation", 0, 360);
        ObjectAnimator oaAlpha = ObjectAnimator.ofFloat(ivProperty, "alpha", 1, 0);
        AnimatorSet as = new AnimatorSet();
        as.play(oaScaleX).with(oaScaleY).with(oaRotation);
        as.play(oaAlpha).after(oaRotation);
        as.setDuration(1000);
        as.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ivProperty.setAlpha(1.0f);
                Log.d(TAG, "finish:" + ivProperty.getAlpha());
            }
        });
        as.start();
    }

    @OnClick(R.id.btn_animator_xml)
    public void skip417() {
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.property_demo);
        animator.setTarget(ivProperty);
        animator.start();

    }
}
