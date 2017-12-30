package example.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.style.base.BaseFragment;
import com.style.framework.R;
import com.style.framework.databinding.FragEmotionInfoBinding;


public class EmotionDataFrag extends BaseFragment {

    FragEmotionInfoBinding bd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //rootView = inflater.inflate(R.layout.frag_emotion_info, null);
        bd = DataBindingUtil.inflate(inflater, R.layout.frag_emotion_info, container, false);
        return bd.getRoot();
    }

    @Override
    protected void initData() {

    }

    protected void initListener() {
    }


}
