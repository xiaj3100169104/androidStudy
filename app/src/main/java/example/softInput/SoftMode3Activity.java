package example.softInput;

import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.style.base.BaseActivityPresenter;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode3Binding;
import com.style.utils.DeviceInfoUtil;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;

import example.adapter.StringAdapter;


public class SoftMode3Activity extends BaseActivity {
    private String TAG = "SoftMode3Activity";

    ActivitySoftMode3Binding bd;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private ArrayList<String> dataList;
    private LinearLayoutManager layoutManager;
    private StringAdapter adapter;

    @Override
    protected int getStatusBarStyle() {
        return STATUS_BAR_TRANSLUCENT;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_soft_mode_3;
    }

    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("半透明状态栏，需要自己手动处理编辑框位置");
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

        //resetSendMsgRl();
    }

    private void resetSendMsgRl() {
        final View decorView = getWindow().getDecorView();
        final Rect rect = new Rect();
        final int screenHeight = DeviceInfoUtil.getScreenHeight(this);
        //阀值设置为屏幕高度的1/3
        final int keyHeight = screenHeight / 3;
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Log.e(TAG, "onGlobalLayout");
            decorView.getWindowVisibleDisplayFrame(rect);
            int heightDifference = screenHeight - rect.bottom;//计算软键盘占有的高度  = 屏幕高度 - 视图可见高度
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) bd.layoutRoot.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, heightDifference);//设置rlContent的marginBottom的值为软键盘占有的高度即可
            bd.layoutRoot.requestLayout();
            if (heightDifference > keyHeight) {
                Log.e(TAG, "监听到软键盘弹起");
                layoutManager.scrollToPosition(adapter.getItemCount() - 1);

            } else {
                Log.e(TAG, "监听到软件盘关闭");

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getData() {
        for (int i = 0; i < 3; i++) {
            dataList.add(i + "");
        }
        adapter.notifyDataSetChanged();
        bd.recyclerView.scrollToPosition(adapter.getItemCount());
    }

}
