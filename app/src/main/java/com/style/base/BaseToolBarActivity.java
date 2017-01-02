package com.style.base;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.style.framework.R;

public abstract class BaseToolBarActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView tvTitleBase;
    private ImageView ivBaseToolbarReturn;

    protected void customTitleOptions(View mContentView) {
        toolbar = (Toolbar) mContentView.findViewById(R.id.toolbar);
        ivBaseToolbarReturn = (ImageView) mContentView.findViewById(R.id.iv_base_toolbar_Return);
        tvTitleBase = (TextView) mContentView.findViewById(R.id.tv_base_toolbar_title);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
        //隐藏Toolbar的标题
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        ivBaseToolbarReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleBack();
            }
        });
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public View getToolbarRightView() {
        return ivBaseToolbarReturn;
    }

    protected void onClickTitleBack() {
        onBackFinish();
    }

    protected void setNavigationIcon(int resId) {
        toolbar.setNavigationIcon(getResources().getDrawable(resId));
    }

    protected void setToolbarTitle(String text) {
        setText(tvTitleBase, text);
    }

    protected void setToolbarTitle(int resId) {
        setText(tvTitleBase, resId);
    }

}
