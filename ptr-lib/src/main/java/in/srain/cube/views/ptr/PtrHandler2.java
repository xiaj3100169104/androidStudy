package in.srain.cube.views.ptr;

import android.view.View;

public interface PtrHandler2 extends PtrHandler{

    /**
     * Check can do load more or not. For example the content is empty or the first child is in view.
     *
     * {@link PtrDefaultHandler#checkContentCanBePulledDown}
     */
    abstract boolean checkCanDoLoadMore(final PtrFrameLayout frame, final View content, final View footer);

    /**
     * When load more begin
     *
     * @param frame
     */
    public abstract void onLoadMoreBegin(final PtrFrameLayout frame);
}