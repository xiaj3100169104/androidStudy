package example.tablayout;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityTabLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutActivity extends BaseActivity {

    ActivityTabLayoutBinding bd;
    private FindTabAdapter fAdapter;                               //定义adapter

    private List<Fragment> fragments = new ArrayList<>();                                //定义要装fragment的列表
    private List<String> titles = new ArrayList<>();
    @Override
    public int getLayoutResId() {
        return R.layout.activity_tab_layout;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("tabLayout");
        for (int i = 1; i < 10; i++) {
            titles.add("title_" + i);
            fragments.add(TabLayoutFragment.newInstance(i + ""));
        }
        fAdapter = new FindTabAdapter(this.getSupportFragmentManager(), fragments, titles);
        bd.viewPager.setAdapter(fAdapter);
        bd.tabLayout.setupWithViewPager(bd.viewPager);
        bd.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
