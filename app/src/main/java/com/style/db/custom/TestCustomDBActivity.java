package com.style.db.custom;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.style.base.BaseActivity;
import com.style.base.BaseToolBarActivity;
import com.style.framework.R;

import java.util.List;

import butterknife.Bind;
import test.bean.User;

public class TestCustomDBActivity extends BaseActivity {
    @Bind(R.id.tv_query_by_id)
    EditText tvQueryById;
    @Bind(R.id.tv_update_by_id)
    EditText tvUpdateById;
    @Bind(R.id.tv_new_password)
    EditText tvNewPassword;
    @Bind(R.id.tv_del_by_id)
    EditText tvDelById;
    private String TAG = getClass().getSimpleName();

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_test_custom_db;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        dbManager = DBManager.getInstance();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.closeDataBase();
    }

    public void insert(View view) {
        long index = 1;
        List<User> list = queryAll();
        if (list != null && list.size() > 0) {
            index = list.get(list.size() - 1).getId();
        }
        User person = new User(index, "account" + index, "password" + index, "userName" + index, null);
        long id = dbManager.insert(person);
        Log.e(TAG, "insert=id=" + id);
    }

    public void update(View view) {
        ContentValues values = new ContentValues();
        values.put("password", tvNewPassword.getText().toString());
        dbManager.updateById(User.class, values, Long.valueOf(tvUpdateById.getText().toString()));
    }

    public void delete(View view) {
        dbManager.deleteById(User.class, Long.valueOf(tvDelById.getText().toString()));
    }

    public void findAll2(View v) {
        queryAll();
    }

    public void findRecord(View v) {
        query(Long.valueOf(tvQueryById.getText().toString()));
    }

    public void query(long id) {
        User user = dbManager.findById(User.class, (int) id);
        if (user != null)
            Log.e(TAG, "findById==" + user.toString());
        else
            Log.e(TAG, "findById==null");

    }

    public List<User> queryAll() {
        List<User> list = dbManager.findAll(User.class);
        if (list != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0, size = list.size(); i < size; i++) {
                sb.append(list.get(i).toString()).append("\n");
            }
            Log.e(TAG, "queryAll==" + sb.toString());
        } else {
            Log.e(TAG, "queryAll==null");
        }
        return list;
    }
}