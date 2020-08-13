package example.viewPagerCards.views;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.style.framework.R;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter {

    private ArrayList<CardItem> mData;

    public CardPagerAdapter(ArrayList<CardItem> list) {
        mData = list;
    }

    @Override
    public int getCount() {
        return mData.size() * 500 + mData.size() + mData.size() * 500;
    }

    public int getNearPosition(int currentItem, int next) {
        int p = currentItem % mData.size();
        if (next == p)
            return currentItem;
        //当前为最后一页，需要滑到第一页
        if (p == mData.size() - 1 && next == 0)
            return currentItem + 1;
        return currentItem - p + next;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //无论是创建view添加到容器中还是销毁view,都是在此方法结束之后执行的
    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_pager_cards_views_adapter, container, false);
        container.addView(view);
        int p = position % mData.size();
        bind(mData.get(p), view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void bind(CardItem item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView contentTextView = (TextView) view.findViewById(R.id.contentTextView);
        titleTextView.setText(item.getTitle());
        contentTextView.setText(item.getText());
    }

}
