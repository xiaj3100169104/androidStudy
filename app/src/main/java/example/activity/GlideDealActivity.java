package example.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.glide.GlideCircleTransform;
import com.style.glide.GlideRectBoundTransform;
import com.style.glide.GlideRoundTransform;

import butterknife.Bind;
import butterknife.OnClick;

public class GlideDealActivity extends BaseActivity {


    @Bind(R.id.iv_1)
    ImageView iv1;
    @Bind(R.id.iv_2)
    ImageView iv2;
    @Bind(R.id.iv_3)
    ImageView iv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_test_realm;
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initData() {
    }

    @OnClick(R.id.btn_1)
    public void skip418() {
        //第一个是上下文，第二个是圆角的弧度
        RequestOptions myOptions = new RequestOptions().transform(new GlideCircleTransform(2, 0xFFFFAEB9));
        Glide.with(this).load(R.mipmap.image_fail).apply(myOptions).into(iv1);
    }

    @OnClick(R.id.btn_2)
    public void skip419() {
        RequestOptions myOptions = new RequestOptions().transform(new GlideRoundTransform(5)).skipMemoryCache(true);
        Glide.with(this).load(R.mipmap.ic_add_photo).apply(myOptions).into(iv2);
    }

    @OnClick(R.id.btn_3)
    public void skip420() {
        RequestOptions myOptions = new RequestOptions().transform(new GlideRectBoundTransform(4, 0xFFFF6347)).skipMemoryCache(true);
        Glide.with(this).load(R.mipmap.empty_photo).apply(myOptions).into(iv3);
    }
}
