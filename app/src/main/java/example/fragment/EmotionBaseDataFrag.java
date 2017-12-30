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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //mLayoutResID = R.layout.frag_emotion_base_info;
        bd = DataBindingUtil.inflate(inflater, R.layout.frag_emotion_base_info, container, false);
        return bd.getRoot();
    }

    @Override
    protected void initData() {

    }


}
