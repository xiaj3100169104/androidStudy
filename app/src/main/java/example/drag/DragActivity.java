package example.drag;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.DragActivityBinding;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;


public class DragActivity extends BaseActivity {
    DragActivityBinding bd;
    private ArrayList<Integer> dataList;
    private LinearLayoutManager layoutManager;
    private DragAdapter adapter;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        bd = DataBindingUtil.setContentView(this, R.layout.drag_activity);
        super.setContentView(bd.getRoot());
    }

    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }

    @Override
    public void initData() {
        setToolbarTitle("拖拽测试");

        dataList = new ArrayList<>();
        adapter = new DragAdapter(getContext(), dataList);
        layoutManager = new LinearLayoutManager(getContext());
        bd.recyclerView.setLayoutManager(layoutManager);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        bd.recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new DragAdapterCallback(adapter));
        helper.attachToRecyclerView(bd.recyclerView);

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<Integer>() {
            @Override
            public void onItemClick(int position, Integer data) {
                showToast(position + "-->  data-->" + data + "-->getdata-->" + dataList.get(position));
            }
        });

        refresh();
    }

    private void refresh() {

        dataList.clear();
        for (int i = 0; i < 5; i++) {
            dataList.add(i);
        }
        adapter.notifyDataSetChanged();
    }

}
