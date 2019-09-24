package example.drag.swipeMenu;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.style.base.activity.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.SwipeMenuActivityBinding;
import com.style.view.diviver.DividerItemDecoration;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class SwipeMenuActivity extends BaseTitleBarActivity {
    SwipeMenuActivityBinding bd;
    private ArrayList<String> dataList;
    private LinearLayoutManager layoutManager;
    private SwipeMenuAdapter adapter;
    private int countDy;
    private boolean isMoving;
    private int xOffset;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.swipe_menu_activity);
        setTitleBarTitle("左滑菜单测试");
        bd = getBinding();
        xOffset = -dp2px(50);
        dataList = new ArrayList<>();
        adapter = new SwipeMenuAdapter(getContext(), dataList, bd.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        bd.recyclerView.setLayoutManager(layoutManager);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        bd.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((position, data) -> {
                    showToast(position + "-->  data-->" + data);
                }
        );
        adapter.setOnSwipeMenuListener(new SwipeMenuAdapter.OnSwipeMenuListener<String>() {
            @Override
            public void onMenuEdit(int position, String data) {
                dataList.set(0, "新加");
                bd.recyclerView.closeMenuFromOpen();
                adapter.notifyDataChanged();
            }

            @Override
            public void onMenuDelete(int position, String data) {
                dataList.remove(position);
                bd.recyclerView.closeMenuFromOpen();
                adapter.notifyDataChanged();
            }
        });
        bd.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                logE("onScrollStateChanged", newState + "");
                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {

                }
            }

            /**
             *
             * @param recyclerView
             * @param dx
             * @param dy 向上滚动为正，向下为负
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                logE("onScrolled", recyclerView.getScrollState() + "   " + dy);
            }
        });
        refresh();

    }

    private void refresh() {
        dataList.clear();
        for (int i = 0; i < 25; i++) {
            dataList.add("数据" + i);
        }
        adapter.notifyDataChanged();
    }

}
