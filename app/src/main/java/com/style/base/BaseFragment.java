package com.style.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.style.app.LogManager;

import org.simple.eventbus.EventBus;

public abstract class BaseFragment extends Fragment {
    protected String TAG = getClass().getSimpleName();

    protected boolean isViewCreated;
    protected boolean isInit;
    protected boolean isVisible;
    private boolean isLazyData;

    protected abstract void initData();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        init();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
        }
        init();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
        }
        init();
    }

    private void init() {
        if (isViewCreated && !isInit) {
            initData();
            isInit = true;
        }
        if (isViewCreated && isVisible && isInit && !isLazyData) {
            lazyData();
            isLazyData = true;
        }
    }

    protected void lazyData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected void skip(Class<?> cls) {
        startActivity(new Intent(getContext(), cls));
    }

    public void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }

    public interface OnGiveUpEditDialogListener {
        void onPositiveButton();

        void onNegativeButton();
    }

    public void showToast(CharSequence str) {
        ((BaseActivity) getActivity()).showToast(str);
    }

    public void showToast(@StringRes int resId) {
        ((BaseActivity) getActivity()).showToast(resId);
    }
}
