package example.viewPagerBanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.style.base.BaseFragment;
import com.style.framework.R;
import com.style.framework.databinding.FragEmotionInfoBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class EmotionDataFrag extends BaseFragment {

    FragEmotionInfoBinding bd;

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_emotion_info, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bd = getBinding(view);
    }

}
