package example.viewPagerCards.views;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ViewPagerCardsViewsActivityBinding;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 无限轮播图的做法就是把Adapter中重写getCount返回一个很大的数字，欺骗viewpager有很多，在对position和数据size取模，
 * 使数据的position和实际的position对应上，然后初始化时把当前position设置到getCount数值的中间，
 * 后续使用setCurrentItem时需要设置到getCurrentItem最近一个对应的位置，否则会造成ANR
 */
public class CardActivity extends BaseTitleBarActivity {

    private ViewPagerCardsViewsActivityBinding bd;
    private ArrayList<CardItem> mData;
    private CardPagerAdapter mCardAdapter;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.view_pager_cards_views_activity);
        setTitleBarTitle("views");
        bd = getBinding();
        bd.cardTypeBtn0.setOnClickListener(v -> {
            setCurrentItem(0);
        });
        bd.cardTypeBtn1.setOnClickListener(v -> {
            setCurrentItem(1);
        });
        bd.cardTypeBtn2.setOnClickListener(v -> {
            setCurrentItem(2);
        });
        bd.cardTypeBtn3.setOnClickListener(v -> {
            setCurrentItem(3);
        });
        mData = new ArrayList<>();
        String s = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.  Etiam eget ligula eu lectus lobortis condimentum.";
        mData.add(new CardItem("card 000", s));
        mData.add(new CardItem("card 001", s));
        mData.add(new CardItem("card 002", s));
        mData.add(new CardItem("card 003", s));
        mCardAdapter = new CardPagerAdapter(mData);
        bd.viewPager.setAdapter(mCardAdapter);
        bd.viewPager.setOffscreenPageLimit(3);
        bd.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             *
             * @param position  左滑时，当前页；右滑时，前一页；当positionOffsetPixels快达到边界值时，position改变.
             * @param positionOffset positionOffsetPixels占viewpager宽度的百分比；
             * @param positionOffsetPixels  左滑时，当前页的左边界到viewpager左边界的距离（如果有padding，不包括padding）；
             *                              右滑时，前一页的左边界到viewpager左边界的距离（如果有padding，不包括padding）；
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                logE("onPageScrolled", "position=" + position + "  positionOffset=" + positionOffset + "   positionOffsetPixels=" + positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int i) {
                //ViewGroup vg = mCardAdapter.getCardViewAt(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        setCurrentItem(mData.size() * 500);
    }

    private void setCurrentItem(int p) {
        bd.viewPager.setCurrentItem(mCardAdapter.getNearPosition(bd.viewPager.getCurrentItem(), p), true);

    }
}
