package example.viewPagerCards.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.style.base.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ViewPagerCardsFragmentsActivityBinding;

import java.util.ArrayList;
import java.util.List;

import example.viewPagerCards.ShadowTransformer;

public class CardFragmentActivity extends BaseDefaultTitleBarActivity implements CompoundButton.OnCheckedChangeListener {

    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    private List<CardFragment> mFragments;
    private ViewPagerCardsFragmentsActivityBinding bd;

    @Override
    protected int getLayoutResId() {
        return R.layout.view_pager_cards_fragments_activity;
    }

    @Override
    protected void initData() {
        setToolbarTitle("fragments");
        bd = getBinding();
        bd.checkBox.setOnCheckedChangeListener(this);
        bd.cardTypeBtn.setOnClickListener(v->{bd.viewPager.setCurrentItem(4, true);});
        mFragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CardFragment f = new CardFragment();
            Bundle b = new Bundle();
            b.putInt("data", i);
            f.setArguments(b);
            mFragments.add(f);
        }
        mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), mFragments, dpToPixels(2, this));
        mFragmentCardShadowTransformer = new ShadowTransformer(bd.viewPager, mFragmentCardAdapter);
        bd.viewPager.setAdapter(mFragmentCardAdapter);
        bd.viewPager.setPageTransformer(false, mFragmentCardShadowTransformer);
        bd.viewPager.setOffscreenPageLimit(3);
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mFragmentCardShadowTransformer.enableScaling(b);
    }
}
