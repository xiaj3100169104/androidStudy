package example.editLayout;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.activity.BaseFullScreenStableTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.EditLayoutMoveBottomOfFullScreenBinding;
import com.style.helper.InputMethodStateListener;
import com.style.view.diviver.DividerItemDecoration;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import example.adapter.StringAdapter;


public class FullScreenBottomEditLayoutActivity extends BaseFullScreenStableTitleBarActivity {

    EditLayoutMoveBottomOfFullScreenBinding bd;

    private ArrayList<String> dataList;
    private StringAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.edit_layout_move_bottom_of_full_screen);
        bd = getBinding();
        setToolbarTitle("调整编辑布局");
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataList = new ArrayList<>();
        adapter = new StringAdapter(this, dataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
