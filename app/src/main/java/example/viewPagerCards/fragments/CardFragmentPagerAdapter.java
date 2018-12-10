package example.viewPagerCards.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import java.util.List;

import example.viewPagerCards.CardAdapter;

public class CardFragmentPagerAdapter extends FragmentPagerAdapter implements CardAdapter {

    private List<CardFragment> mFragments;
    private float mBaseElevation;

    public CardFragmentPagerAdapter(FragmentManager fm, List<CardFragment> mFragments, float baseElevation) {
        super(fm);
        this.mFragments = mFragments;
        mBaseElevation = baseElevation;
    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mFragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        mFragments.set(position, (CardFragment) fragment);
        return fragment;
    }

}
