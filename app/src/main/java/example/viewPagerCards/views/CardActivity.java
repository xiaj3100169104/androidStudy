package example.viewPagerCards.views;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ViewPagerCardsViewsActivityBinding;

import org.jetbrains.annotations.Nullable;

public class CardActivity extends BaseTitleBarActivity {

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer2 mCardShadowTransformer;
    private ViewPagerCardsViewsActivityBinding bd;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.view_pager_cards_views_activity);
        setTitleBarTitle("views");
        bd = getBinding();
        bd.cardTypeBtn.setOnClickListener(v -> {
            bd.viewPager.setCurrentItem(3);
        });
        String s = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.  Etiam eget ligula eu lectus lobortis condimentum.";
        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(new CardItem("Cool card", s));
        mCardAdapter.addCardItem(new CardItem("Ok card", s));
        mCardAdapter.addCardItem(new CardItem("Bad card", s));
        mCardAdapter.addCardItem(new CardItem("Ugly card", s));
        //mCardShadowTransformer = new ShadowTransformer2(bd.viewPager, mCardAdapter);
        bd.viewPager.setAdapter(mCardAdapter);
        //bd.viewPager.setPageTransformer(false, mCardShadowTransformer);
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

    }
}
