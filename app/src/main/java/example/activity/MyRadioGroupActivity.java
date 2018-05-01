package example.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityMyRadioGroupBinding;
import com.style.manager.ToastManager;
import com.style.view.MyRadioGroup;

import example.fragment.EmotionBaseDataFrag;
import example.fragment.EmotionDataFrag;

/**
 * Created by xiajun on 2016/10/8.
 */
public class MyRadioGroupActivity extends BaseActivity {
    ActivityMyRadioGroupBinding bd;
    private MyRadioGroup rg_emotion;
    private FragmentManager fm;
    private FragmentTransaction bt;
    private EmotionBaseDataFrag baseDataFrag;
    private EmotionDataFrag emoDataFrag;
    private Fragment[] frags;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_my_radio_group);
        super.setContentView(bd.getRoot());
    }

    @Override
    public void initData() {
        setToolbarTitle("切换");
        rg_emotion = (MyRadioGroup) this.findViewById(R.id.rg_emotion_card_tab);
        fm = getSupportFragmentManager();
        baseDataFrag = new EmotionBaseDataFrag();
        /*Bundle bd = new Bundle();
        bd.putSerializable(Skip.EMODATA_KEY, emoData);
        baseDataFrag.setArguments(bd);*/

        emoDataFrag = new EmotionDataFrag();

        frags = new Fragment[]{baseDataFrag, emoDataFrag};
        bt = fm.beginTransaction();
        for (int i = 0; i < frags.length; i++) {
            bt.add(bd.rlContainer.getId(), frags[i]).hide(frags[i]);
        }
        bt.show(frags[0]).commit();
        rg_emotion.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                int index = 0;
                switch (checkedId) {
                    case R.id.rb_base_data:
                        index = 0;
                        ToastManager.showToast(MyRadioGroupActivity.this,"activity");
                        break;
                    case R.id.rb_emotion_data:
                        index = 1;
                        ToastManager.showToastOnApplication("application");
                        break;
                }
                changeFrag(index);
            }
        });
    }

    private void changeFrag(int index) {
        bt = fm.beginTransaction();
        for (int i = 0; i < frags.length; i++) {
            if (i == index)
                bt.show(frags[i]);
            else
                bt.hide(frags[i]);
        }
        bt.commit();
    }
}