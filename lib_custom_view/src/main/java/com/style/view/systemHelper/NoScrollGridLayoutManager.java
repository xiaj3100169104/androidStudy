package com.style.view.systemHelper;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;

public class NoScrollGridLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = true;

    public NoScrollGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
