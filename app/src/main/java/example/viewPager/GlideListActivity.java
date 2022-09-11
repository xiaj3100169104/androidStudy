package example.viewPager;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dmcbig.mediapicker.entity.Media;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.GlideListTestBinding;
import com.style.view.diviver.DividerItemDecoration;

import java.util.ArrayList;


public class GlideListActivity extends BaseTitleBarActivity {

    GlideListTestBinding bd;
    private ArrayList<Media> dataList;
    private GlideListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = GlideListTestBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setTitleBarTitle("glide测试");

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
