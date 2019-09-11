package example.viewPagerCards.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.style.base.activity.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ViewPagerCardsViewsActivityBinding;

import org.jetbrains.annotations.Nullable;

import example.viewPagerCards.ShadowTransformer;

public class CardActivity extends BaseDefaultTitleBarActivity {

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private ViewPagerCardsViewsActivityBinding bd;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.view_pager_cards_views_activity);
        setToolbarTitle("views");
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
        mCardShadowTransformer = new ShadowTransformer(bd.viewPager, mCardAdapter);
        bd.viewPager.setAdapter(mCardAdapter);
        //bd.viewPager.setPageTransformer(false, mCardShadowTransformer);
        bd.viewPager.setOffscreenPageLimit(3);
        bd.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                ViewGroup vg = mCardAdapter.getCardViewAt(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }
}
