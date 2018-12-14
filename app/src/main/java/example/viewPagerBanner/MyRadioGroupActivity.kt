package example.viewPagerBanner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager

import com.style.base.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.BannerActivityBinding
import com.style.view.systemHelper.MyRadioGroup;

/**
 * Created by xiajun on 2016/10/8.
 */
class MyRadioGroupActivity : BaseDefaultTitleBarActivity() {
    private lateinit var bd: BannerActivityBinding
    private lateinit var rg_emotion: MyRadioGroup;
    private lateinit var fm: FragmentManager;
    private lateinit var bt: FragmentTransaction;
    private lateinit var baseDataFrag: EmotionBaseDataFrag;
    private lateinit var emoDataFrag: EmotionDataFrag;
    private lateinit var frags: Array<Fragment>;
    private lateinit var bannerFrags: ArrayList<BannerFragment>;
    private lateinit var fAdapter: BannerAdapter                            //定义adapter


    override fun getLayoutResId(): Int {
        return R.layout.banner_activity
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

        bannerFrags = ArrayList()
        bannerFrags.add(BannerFragment.newInstance(R.mipmap.home_banner_1))
        bannerFrags.add(BannerFragment.newInstance(R.mipmap.home_banner_2))
        bannerFrags.add(BannerFragment.newInstance(R.mipmap.home_banner_3))
        fAdapter = BannerAdapter(this.supportFragmentManager, bannerFrags)
        bd.viewPager.adapter = fAdapter
        bd.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                var id: Int = bd.bannerGroup.getChildAt(p0).id
                bd.bannerGroup.setCheckWithoutNotif(id)
            }
        })

        bd.bannerGroup.setOnCheckedChangeListener { group, checkedId ->
            when (bd.bannerGroup.checkedRadioButtonId) {
                bd.banner1.id -> bd.viewPager.setCurrentItem(0, false)
                bd.banner2.id -> bd.viewPager.setCurrentItem(1, false)
                bd.banner3.id -> bd.viewPager.setCurrentItem(2, false)
            }
        }
    }


    fun changeFrag(index: Int) {
        bt = fm.beginTransaction()
        for (i in frags.indices) {
            if (i == index)
                bt.show(frags[i])
            else
                bt.hide(frags[i])
        }
        bt.commit()
    }
}