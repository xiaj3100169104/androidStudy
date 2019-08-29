package com.xiajun.xxrefreshview.header;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiajun.xxrefreshview.R;
import com.xiajun.xxrefreshview.XXRefreshHeader;
import com.xiajun.xxrefreshview.XXRefreshView;

/**
 * Created by xiajun on 2018/9/14.
 */

public class XXRefreshDefaultHeader extends RelativeLayout implements XXRefreshHeader {
    protected final String TAG = getClass().getSimpleName();
    private TextView tvHeader;
    private int state;
    private RefreshCircleAProgressBar progressBar;
    //假设旋转一度需要下拉的距离，单位：px
    int distanceOfAngle = 1;
    private int mStartAngle;

    public XXRefreshDefaultHeader(@NonNull Context context) {
        super(context);
        initView(context);
    }


    public XXRefreshDefaultHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public XXRefreshDefaultHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public XXRefreshDefaultHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        RelativeLayout popView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.xxrefresh_default_header, null);
        progressBar = popView.findViewById(R.id.xxrefresh_default_progress_bar);
        tvHeader = popView.findViewById(R.id.xxrefresh_default_header_tv);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(popView, 0, params);
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
                progressBar.setmIndeterminate(true);
                break;
        }

    }

    @Override
    public void initHeight(int heightSlop, int total) {
        Log.e(TAG, "--" + heightSlop + "--" + total);

    }

    @Override
    public void onVisibleHeightChanged(int height) {
        if (state != XXRefreshView.STATE_HEADER_REFRESH) {
            Log.e(TAG, "--" + height);
            progressBar.setmIndeterminate(false);
            mStartAngle = -height / distanceOfAngle % 360;
            progressBar.setStartAngle(mStartAngle);

        }
    }
}
