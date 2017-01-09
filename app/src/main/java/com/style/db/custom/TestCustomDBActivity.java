package com.style.db.custom;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.style.base.BaseActivity;
import com.style.bean.Friend;
import com.style.framework.R;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import com.style.bean.User;

public class TestCustomDBActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_test_custom_db;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
    }

}