package com.style.view.refresh;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.style.framework.R;

/**
 * Created by xiajun on 2018/9/14.
 */

public class XXRefreshDefaultHeader extends FrameLayout implements XXRefreshHeader {
    protected final String TAG = getClass().getSimpleName();
    private TextView tvHeader;
    private int state;

    public XXRefreshDefaultHeader(@NonNull Context context) {
        super(context);
        initD(context);
    }


    public XXRefreshDefaultHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initD(context);

    }

    public XXRefreshDefaultHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initD(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public XXRefreshDefaultHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initD(context);

    }

    private void initD(Context context) {
        RelativeLayout popView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.xxrefresh_default_header, null);
        tvHeader = popView.findViewById(R.id.tv_xxrefresh_default_header);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void onState(int state) {
        this.state = state;
        switch (state) {
            case XXRefreshView.STATE_HEADER_PULL_DOWN_NOT_CAN_REFRESH:
                tvHeader.setText("下拉可以刷新");
                break;
            case XXRefreshView.STATE_HEADER_PULL_DOWN_RELEASE_CAN_REFRESH:
                tvHeader.setText("释放可以刷新");
                break;
            case XXRefreshView.STATE_HEADER_REFRESH:
                tvHeader.setText("正在刷新");
                break;
        }

    }

    @Override
    public void onVisibleHeight(int height, int total) {
        if (state != XXRefreshView.STATE_HEADER_REFRESH) {
            Log.e(TAG, height + "--" + total);
        }
    }
}
