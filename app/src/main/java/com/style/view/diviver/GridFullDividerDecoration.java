package com.style.view.diviver;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.style.framework.R;

/**
 * 等间距网格分割线
 * Created by xiajun on 2018/8/30.
 */

public class GridFullDividerDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "GridFullDivider";
    private final int spanCount;
    private int dividerHeight = 5;
    private Paint dividerPaint;

    public GridFullDividerDecoration(Context context, int spanCount) {
        this.spanCount = spanCount;
        dividerPaint = new Paint();
        dividerPaint.setColor(context.getResources().getColor(R.color.divider_color));
    }

    public GridFullDividerDecoration(Context context, int spanCount, int color, int dh) {
        this.spanCount = spanCount;
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
        dividerHeight = dh;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        Log.e(TAG, "getItemOffsets  " + position);
        super.getItemOffsets(outRect, view, parent, state);
        if (position < spanCount) {
            outRect.top = dividerHeight;
        }
        if (position % spanCount == 0) {
            outRect.left = dividerHeight;
        }
        outRect.right = dividerHeight;
        outRect.bottom = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        Log.e(TAG, "onDraw  ");
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            float left = view.getLeft();
            float top = view.getTop();
            float right = view.getRight();
            float bottom = view.getBottom();
            if (i < spanCount) {
                c.drawRect(left - dividerHeight, top - dividerHeight, right + dividerHeight, bottom, dividerPaint);
            }
            if (i % spanCount == 0) {
                c.drawRect(left - dividerHeight, top - dividerHeight, left, bottom + dividerHeight, dividerPaint);
            }
            c.drawRect(right, top - dividerHeight, right + dividerHeight, bottom + dividerHeight, dividerPaint);
            c.drawRect(left - dividerHeight, bottom, right + dividerHeight, bottom + dividerHeight, dividerPaint);
        }
    }
}

