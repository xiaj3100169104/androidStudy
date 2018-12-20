package example.viewPagerCards.views;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.style.base.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ViewPagerCardsViewsActivityBinding;

import org.jetbrains.annotations.Nullable;

import example.viewPagerCards.ShadowTransformer;

public class CardActivity extends BaseDefaultTitleBarActivity implements CompoundButton.OnCheckedChangeListener {

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private ViewPagerCardsViewsActivityBinding bd;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.view_pager_cards_views_activity);
        setToolbarTitle("views");
        bd = getBinding();
        bd.checkBox.setOnCheckedChangeListener(this);
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
        bd.viewPager.setPageTransformer(false, mCardShadowTransformer);
        bd.viewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mCardShadowTransformer.enableScaling(b);
    }
}
