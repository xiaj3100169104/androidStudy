package example.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityMyRadioGroupBinding;
import com.style.view.MyRadioGroup;

import example.fragment.EmotionBaseDataFrag;
import example.fragment.EmotionDataFrag;

/**
 * Created by xiajun on 2016/10/8.
 */
class MyRadioGroupActivity : BaseTitleBarActivity() {
    private lateinit var bd: ActivityMyRadioGroupBinding
    private lateinit var rg_emotion: MyRadioGroup;
    private lateinit var fm: FragmentManager;
    private lateinit var bt: FragmentTransaction;
    private lateinit var baseDataFrag: EmotionBaseDataFrag;
    private lateinit var emoDataFrag: EmotionDataFrag;
    private lateinit var frags: Array<Fragment>;
    override fun getLayoutResId(): Int {
        return R.layout.activity_my_radio_group
    }

    override fun initData() {
        bd = getBinding();
        setToolbarTitle("切换");
        rg_emotion = findViewById(R.id.rg_emotion_card_tab);
        fm = getSupportFragmentManager();
        baseDataFrag = EmotionBaseDataFrag();
        /*Bundle bd = new Bundle();
        bd.putSerializable(Skip.EMODATA_KEY, emoData);
        baseDataFrag.setArguments(bd);*/

        emoDataFrag = EmotionDataFrag();

        frags = arrayOf(baseDataFrag, emoDataFrag);
        bt = fm.beginTransaction();
        for (i in frags.indices) {
            bt.add(bd.rlContainer.id, frags[i]).hide(frags[i]);
        }
        bt.show(frags[0]).commit();
        rg_emotion.setOnCheckedChangeListener(object : MyRadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: MyRadioGroup, checkedId: Int) {
                var index = 0;
                when (checkedId) {
                    R.id.rb_base_data -> {
                        index = 0;
                        showToast("activity");
                    }
                    R.id.rb_emotion_data ->
                        index = 1;
                }
                changeFrag(index);
            }
        });
    }

    fun changeFrag(index: Int) {
        bt = fm.beginTransaction();
        for (i in frags.indices) {
            if (i == index)
                bt.show(frags[i]);
            else
                bt.hide(frags[i]);
        }
        bt.commit();
    }
}