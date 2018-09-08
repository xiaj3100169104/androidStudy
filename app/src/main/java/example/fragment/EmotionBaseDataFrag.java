package example.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.style.base.BaseFragment;
import com.style.framework.R;
import com.style.framework.databinding.FragEmotionBaseInfoBinding;

public class EmotionBaseDataFrag extends BaseFragment {

    FragEmotionBaseInfoBinding bd;

    @Override
    protected int getLayoutResId() {
        return R.layout.frag_emotion_base_info;
    }

    @Override
    protected void initData() {
        bd = getBinding();
    }

}
