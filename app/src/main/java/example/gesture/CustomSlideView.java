package example.gesture;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by xiajun on 2018/9/14.
 */

public class CustomSlideView extends View {
    protected final String TAG = getClass().getSimpleName();

    private boolean mIsBeingDragged = false;
    boolean isScrollFinished = true;
    /**
     * Position of the last motion event.
     */
    private int mLastMotionX;
    private int mTouchSlop;

    public CustomSlideView(@NonNull Context context) {
        super(context);
        initD(context);
    }


    public CustomSlideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initD(context);

    }

    public CustomSlideView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initD(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomSlideView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initD(context);

    }

    private void initD(Context context) {
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        Log.e(TAG, "onTouchEvent   " + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (getParent() != null) getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return true;
    }
}
