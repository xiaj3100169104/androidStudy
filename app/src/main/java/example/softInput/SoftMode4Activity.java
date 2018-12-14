package example.softInput;

import android.support.v7.widget.LinearLayoutManager;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.BaseWhiteTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode4Binding;
import com.style.helper.InputMethodStateListener;
import com.style.view.systemHelper.DividerItemDecoration;

import java.util.ArrayList;

import example.adapter.StringAdapter;
import example.gesture.BaseRightSlideFinishActivity;


public class SoftMode4Activity extends BaseWhiteTitleBarActivity {

    ActivitySoftMode4Binding bd;

    private ArrayList<String> dataList;
    private LinearLayoutManager layoutManager;
    private StringAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_soft_mode_4;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("只移动编辑框布局");

    }

    @Override
    protected void onStart() {
        super.onStart();
        dataList = new ArrayList<>();
        adapter = new StringAdapter(this, dataList);
        layoutManager = new LinearLayoutManager(this);
        bd.recyclerView.setLayoutManager(layoutManager);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(this));
        bd.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {

            }
        });
        getData();
        addListener();
    }

    private void addListener() {
        bd.layoutRoot.getViewTreeObserver().addOnGlobalLayoutListener(new InputMethodStateListener(bd.layoutRoot) {
            @Override
            protected void onInputMethodClosed() {

            }

            @Override
            protected void onInputMethodOpened(int invisibleHeight) {

            }
        });
    }

    private void getData() {
        for (int i = 0; i < 3; i++) {
            dataList.add(i + "");
        }
        adapter.notifyDataSetChanged();

    }

}
