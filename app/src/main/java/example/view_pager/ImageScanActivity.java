package example.view_pager;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.dmcbig.mediapicker.entity.Media;
import com.style.framework.R;
import com.style.framework.databinding.ActivityImageScanBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmcBig on 2017/8/9.
 */

public class ImageScanActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    ActivityImageScanBinding bd;
    ArrayList<Media> preRawList;
    private ArrayList<ScanFragment> fragmentArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        bd = DataBindingUtil.setContentView(this, R.layout.activity_image_scan);
        preRawList = getIntent().getParcelableArrayListExtra("list");
        setView(preRawList);
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    void setView(ArrayList<Media> default_list) {
        bd.tvIndex.setText(1 + "/" + preRawList.size());
        fragmentArrayList = new ArrayList<>();
        for (Media media : default_list) {
            fragmentArrayList.add(ScanFragment.newInstance(media, ""));
        }
        AdapterFragment adapterFragment = new AdapterFragment(getSupportFragmentManager(), fragmentArrayList);
        bd.viewpager.setAdapter(adapterFragment);
        bd.viewpager.addOnPageChangeListener(this);
    }

    public class AdapterFragment extends FragmentStatePagerAdapter {
        private List<ScanFragment> mFragments;

        public AdapterFragment(FragmentManager fm, ArrayList<ScanFragment> mFragments) {
            super(fm);
            this.mFragments = mFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        bd.tvIndex.setText((position + 1) + "/" + preRawList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
