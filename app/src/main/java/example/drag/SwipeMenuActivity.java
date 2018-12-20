package example.drag;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

import com.style.base.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.SwipeMenuActivityBinding;
import com.style.view.systemHelper.DividerItemDecoration;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class SwipeMenuActivity extends BaseDefaultTitleBarActivity {
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
        setToolbarTitle("左滑菜单测试");
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
                    resetDy();
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
                changeDy(dy);
            }
        });
        refresh();

    }

    private void resetDy() {
        countDy = 0;
    }

    private void changeDy(int dy) {
        countDy += dy;
        if (countDy >= 150 && !isMoving && bd.view1.getTranslationY() != xOffset) {
            logE(getTAG(), "关闭");
            isMoving = true;
            hideTitleBar();
        } else if (countDy <= -50 && !isMoving && bd.view1.getTranslationY() != 0) {
            logE(getTAG(), "打开");
            isMoving = true;
            showTitleBar();
        }
    }

    private void hideTitleBar() {
        ValueAnimator va = ValueAnimator.ofFloat(0f, xOffset);
        va.setDuration(300);
        //插值器，表示值变化的规律，默认均匀变化
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(animation -> {
            float v = (float) animation.getAnimatedValue();
            Log.d(getTAG(), "translationX:" + v);
            translationTitleBarY(v);
            if (v == xOffset) {
                isMoving = false;
                resetDy();
            }
        });
        va.start();
    }

    private void showTitleBar() {
        ValueAnimator va = ValueAnimator.ofFloat(xOffset, 0f);
        va.setDuration(300);
        //插值器，表示值变化的规律，默认均匀变化
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(animation -> {
            float v = (float) animation.getAnimatedValue();
            Log.d(getTAG(), "translationX:" + v);
            translationTitleBarY(v);
            if (v == 0) {
                isMoving = false;
                resetDy();
            }
        });
        va.start();
    }

    private void translationTitleBarY(float v) {
        bd.view1.setTranslationY(v);
        bd.recyclerView.setTranslationY(v);
    }

    private void refresh() {
        dataList.clear();
        for (int i = 0; i < 25; i++) {
            dataList.add("数据" + i);
        }
        adapter.notifyDataChanged();
    }

}
