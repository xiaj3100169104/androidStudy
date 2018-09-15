package example.drag;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.DragActivityBinding;
import com.style.framework.databinding.SwipeMenuActivityBinding;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;


public class SwipeMenuActivity extends BaseTitleBarActivity {
    SwipeMenuActivityBinding bd;
    private ArrayList<String> dataList;
    private LinearLayoutManager layoutManager;
    private SwipeMenuAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.swipe_menu_activity;
    }


    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("左滑菜单测试");
        dataList = new ArrayList<>();
        adapter = new SwipeMenuAdapter(getContext(), dataList);
        layoutManager = new LinearLayoutManager(getContext());
        bd.recyclerView.setLayoutManager(layoutManager);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        bd.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((position, data) -> {
                    showToast(position + "-->  data-->" + data);
                }
        );
        bd.recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                View view = rv.findChildViewUnder(e.getX(), e.getY());//根据用户点击的坐标，找到RecyclerView下的子View，这里也就是每一个Item
                SwipeMenuAdapter.ViewHolder viewHolder = (SwipeMenuAdapter.ViewHolder) rv.getChildViewHolder(view);//获得每一个Item的ViewHolder
                SwipeMenuView itemLayout = viewHolder.bd.layoutRoot;//获得ViewHolder相应的布局
                itemLayout.setTranslationX(-150);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        refresh();

    }

    private void refresh() {

        dataList.clear();
        for (int i = 0; i < 25; i++) {
            dataList.add("数据" + i);
        }
        adapter.notifyDataSetChanged();
    }

}
