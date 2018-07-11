package example.softInput;

import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.style.base.BaseActivityPresenter;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode3Binding;
import com.style.framework.databinding.ActivitySoftMode4Binding;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;

import example.adapter.StringAdapter;


public class SoftMode4Activity extends BaseActivity {
    private String TAG = "SoftMode4Activity";

    ActivitySoftMode4Binding bd;

    private ArrayList<String> dataList;
    private LinearLayoutManager layoutManager;
    private StringAdapter adapter;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_soft_mode_4;
    }
    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("布局调整");

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

        //addListener();
        resetSendMsgRl();
    }

    private void resetSendMsgRl() {

        final View decorView = getWindow().getDecorView();
        final Rect rect = new Rect();
        final int screenHeight = getScreenHeight();
        //阀值设置为屏幕高度的1/3
        final int keyHeight = screenHeight / 3;
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.e(TAG, "onGlobalLayout");
                decorView.getWindowVisibleDisplayFrame(rect);
                int heightDifference = screenHeight - rect.bottom;//计算软键盘占有的高度  = 屏幕高度 - 视图可见高度
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) bd.layoutRoot.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, heightDifference);//设置rlContent的marginBottom的值为软键盘占有的高度即可
                bd.layoutRoot.requestLayout();
                if (heightDifference > keyHeight) {
                    Log.e("onGlobalLayout", "监听到软键盘弹起");
                    layoutManager.scrollToPosition(adapter.getItemCount() - 1);

                } else {
                    Log.e("onGlobalLayout", "监听到软件盘关闭");

                }
            }
        });
    }


    private int getScreenHeight() {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int height = outMetrics.heightPixels;
        return height;
    }

    private void addListener() {
        //获取屏幕高度
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        final int keyHeight = screenHeight / 3;
        //添加layout大小发生改变监听器,前提是windowSoftInputMode="adjustResize" 并且布局确实会发生大小变化
        bd.layoutRoot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    Log.e(TAG, "监听到软键盘弹起");
                    layoutManager.scrollToPosition(adapter.getItemCount() - 1);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    Log.e(TAG, "监听到软件盘关闭");
                    //如果是切换到表情面板而隐藏流量输入法，需要延迟判断表情面板是否显示，如果表情面板是关闭的，操作栏也关闭
                    /**
                     * Convenience method to scroll to a certain position.
                     *
                     * RecyclerView does not implement scrolling logic, rather forwards the call to
                     * {@link RecyclerView.LayoutManager#scrollToPosition(int)}
                     * @param position Scroll to this adapter position
                     * @see RecyclerView.LayoutManager#scrollToPosition(int)
                     */
                }
            }
        });
    }

    private void getData() {
        for (int i = 0; i < 20; i++) {
            dataList.add(i + "");
        }
        adapter.notifyDataSetChanged();

    }

}
