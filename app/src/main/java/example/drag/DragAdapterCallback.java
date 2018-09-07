package example.drag;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.utils.DeviceInfoUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by xiajun on 2018/5/15.
 */

public class DragAdapterCallback extends ItemTouchHelper.Callback {
    private final String TAG = getClass().getSimpleName();
    private final DragAdapter mAdapter;
    private int lastAction = ItemTouchHelper.ACTION_STATE_IDLE;

    public DragAdapterCallback(DragAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /**
     * 官方文档如下：返回true 当前tiem可以被拖动到目标位置后，直接”落“在target上，其他的上面的tiem跟着“落”，
     * 所以要重写这个方法，不然只是拖动的tiem在动，target tiem不动，静止的
     * Return true if the current ViewHolder can be dropped over the the target ViewHolder.
     */
    @Override
    public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.LEFT;
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, swipeFlags);
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.e(TAG, "onMove");
        int from = viewHolder.getAdapterPosition();
        int to = target.getAdapterPosition();
        Collections.swap(mAdapter.getList(), from, to);
        for (Integer i : mAdapter.getList()) {
            Log.e(TAG, "" + i);
        }
        //这个方法同时也会移动数据源在list集合中的位置
        mAdapter.notifyItemMoved(from, to);
        mAdapter.setOnItemClickListener(viewHolder.itemView, to);
        mAdapter.setOnItemClickListener(target.itemView, from);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.e(TAG, "onSwiped");
        DragAdapter.ViewHolder holder = (DragAdapter.ViewHolder) viewHolder;
        holder.bd.layoutFore.setTranslationX(0);

        int position = viewHolder.getAdapterPosition();
        int temp = mAdapter.getList().remove(position);
        mAdapter.notifyDataSetChanged();
        mAdapter.getList().add(position, temp);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        Log.e(TAG, "onSelectedChanged--actionState-->" + actionState);
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG)
            lastAction = ItemTouchHelper.ACTION_STATE_DRAG;
        else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
            lastAction = ItemTouchHelper.ACTION_STATE_SWIPE;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        Log.e(TAG, "clearView--actionState-->" + lastAction);
        super.clearView(recyclerView, viewHolder);
        //拖动完成
        if (lastAction == ItemTouchHelper.ACTION_STATE_DRAG) {
            for (Integer i : mAdapter.getList()) {
                Log.e(TAG, "" + i);
            }
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Log.e(TAG, "onChildDraw--dx-->" + dX + "--actionState-->" + actionState + "--isCurrentlyActive-->" + isCurrentlyActive);
        if ((actionState == ItemTouchHelper.ACTION_STATE_SWIPE)) {
            DragAdapter.ViewHolder holder = (DragAdapter.ViewHolder) viewHolder;
            //if (Math.abs(dX) <= DeviceInfoUtil.dp2px(recyclerView.getContext(), 200))
            holder.bd.layoutFore.setTranslationX(dX);
            // else
            //  holder.bd.layoutFore.setTranslationX(0.0f);
            super.onChildDraw(c, recyclerView, viewHolder, 0, dY, actionState, isCurrentlyActive);

        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

}
