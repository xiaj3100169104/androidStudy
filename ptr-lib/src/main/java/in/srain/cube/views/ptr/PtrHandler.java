package in.srain.cube.views.ptr;

import android.view.View;

import in.srain.cube.views.ptr.indicator.PtrIndicator;

public interface PtrHandler {

    /**
     * Check can do refresh or not. For example the content is empty or the first child is in view.
     *
     * {@link in.srain.cube.views.ptr.PtrDefaultHandler#checkContentCanBePulledDown}
     */
    abstract boolean checkCanDoRefresh(final PtrFrameLayout frame, final View content, final View header);

    /**
     * When refresh begin
     *
     * @param frame
     */
    public abstract void onRefreshBegin(final PtrFrameLayout frame);
}