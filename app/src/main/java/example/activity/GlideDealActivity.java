package example.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.framework.R;
import com.style.framework.databinding.ActivityGlideDealBinding;
import com.style.glide.GlideCircleTransform;
import com.style.glide.GlideRectBoundTransform;
import com.style.glide.GlideRoundTransform;


public class GlideDealActivity extends BaseActivity {

    private ActivityGlideDealBinding bd;

    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.activity_glide_deal;
    }

    @Override
    public void initData() {
        bd = getBinding();

    }

    public void skip418(View v) {
        //第一个是上下文，第二个是圆角的弧度
        RequestOptions myOptions = new RequestOptions().transform(new GlideCircleTransform(2, 0xFFFFAEB9));
        Glide.with(this).load(R.mipmap.image_fail).apply(myOptions).into(bd.iv1);
    }

    public void skip419(View v) {
        RequestOptions myOptions = new RequestOptions().transform(new GlideRoundTransform(5)).skipMemoryCache(true);
        Glide.with(this).load(R.mipmap.ic_add_photo).apply(myOptions).into(bd.iv2);
    }

    public void skip420(View v) {
        RequestOptions myOptions = new RequestOptions().transform(new GlideRectBoundTransform(4, 0xFFFF6347)).skipMemoryCache(true);
        Glide.with(this).load(R.mipmap.empty_photo).apply(myOptions).into(bd.iv3);
    }
}
