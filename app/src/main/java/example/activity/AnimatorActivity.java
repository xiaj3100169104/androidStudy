package example.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;

import butterknife.Bind;
import butterknife.OnClick;

public class AnimatorActivity extends BaseToolBarActivity {

    @Bind(R.id.btn_PropertyAnimation)
    Button btnPropertyAnimation;
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

    @OnClick(R.id.btn_PropertyAnimation)
    public void skip414() {
        float fromX = ivProperty.getTranslationX();
        ValueAnimator va = ValueAnimator.ofFloat(fromX, fromX + 100f, fromX);
        va.setDuration(1000);

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

}
