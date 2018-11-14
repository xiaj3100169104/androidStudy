package example.customview.fragment;

import android.graphics.Color;
import android.view.View;

import com.style.base.BaseFragment;
import com.style.framework.R;
import com.style.view.other.CustomNotifyView;


public class CustomNotifyViewFragment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_notifyview;
    }

    @Override
    protected void initData() {
        CustomNotifyView customNotifyView = (CustomNotifyView) getView().findViewById(R.id.custom);
        customNotifyView.setNotifyText("99");
        customNotifyView.setNotifyTextColor(Color.BLACK);

        final CustomNotifyView view2 = (CustomNotifyView) getView().findViewById(R.id.view_change);
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view2.setNotifyCount(10);
            }
        });
    }
}
