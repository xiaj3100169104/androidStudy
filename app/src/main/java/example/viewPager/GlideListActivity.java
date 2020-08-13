package example.viewPager;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dmcbig.mediapicker.entity.Media;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.GlideListTestBinding;
import com.style.view.diviver.DividerItemDecoration;

import java.util.ArrayList;


/**
 * Created by dmcBig on 2017/6/9.
 */

public class GlideListActivity extends AppCompatActivity {

    GlideListTestBinding bd;
    private ArrayList<Media> dataList;
    private GlideListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.glide_list_test);

        dataList = new ArrayList<>();
        dataList = getIntent().getParcelableArrayListExtra("list");

        adapter = new GlideListAdapter(this, dataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        bd.recyclerView.setLayoutManager(layoutManager);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(this));
        bd.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {

            }
        });
    }


}
