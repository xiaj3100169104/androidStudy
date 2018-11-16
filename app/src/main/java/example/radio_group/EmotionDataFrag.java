package example.radio_group;

import com.style.base.BaseFragment;
import com.style.framework.R;
import com.style.framework.databinding.FragEmotionInfoBinding;


public class EmotionDataFrag extends BaseFragment {

    FragEmotionInfoBinding bd;

    @Override
    protected int getLayoutResId() {
        return R.layout.frag_emotion_info;
    }

    @Override
    protected void initData() {
        bd = getBinding();
    }

    protected void initListener() {

    }

}
