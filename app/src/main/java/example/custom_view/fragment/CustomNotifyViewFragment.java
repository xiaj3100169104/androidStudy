package example.custom_view.fragment;

import com.style.base.BaseFragment;
import com.style.framework.R;
import com.style.view.other.CustomNotifyView;

import java.util.Random;


public class CustomNotifyViewFragment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_notifyview;
    }

    @Override
    protected void initData() {
        Random random = new Random();
        CustomNotifyView customNotifyView = getView().findViewById(R.id.custom);
        customNotifyView.setOnClickListener(view -> customNotifyView.setNotifyCount(random.nextInt(99)));
    }
}
