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

    private String TAG = getClass().getSimpleName();

    private UserDBManager myTableManager;
    private User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_test_custom_db;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        myTableManager = UserDBManager.getInstance();
        login();
        List<Friend> list = myTableManager.queryAllFriend();
        if (list != null && list.size() > 0)
            return;
      new Thread(new Runnable() {
          @Override
          public void run() {
              for (int i = 9; i < 150; i++) {
                  User user = new User(i, "phone" + i, "123456", "用户" + i, null);
                  myTableManager.insertUser(user);


              }
          }
      }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 9; i < 150; i++) {
                    Friend bean = new Friend();
                    bean.setFriendId(i);
                    if (i < 12)
                        bean.setOwnerId(8);
                    else
                        bean.setOwnerId(4);
                    myTableManager.insertFriendUser(bean);
                }
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myTableManager.closeDB();
    }

    @OnClick(R.id.view_get_all_user)
    public void get_all_user() {
        myTableManager.queryAllUser();
    }

    @OnClick(R.id.view_get_all_friend)
    public void get_all_ufriend() {
        myTableManager.queryAllFriend();
    }

    @OnClick(R.id.view_delete_current_user)
    public void delete() {
        myTableManager.deleteUser(curUser.getUserId());
        curUser = null;
    }

    @OnClick(R.id.view_login)
    public void login() {
        curUser = myTableManager.queryUser(8);
        if (curUser == null) {
            curUser = new User(8, "18202823096", "123456", "夏军", null);
            myTableManager.insertOrUpdateUser(curUser);
        }
    }

    @OnClick(R.id.view_getFriend)
    public void getFriend() {
        myTableManager.queryAllFriend(curUser.getUserId());

    }

    @OnClick(R.id.view_getFriendUser)
    public void getFriendUser() {
        myTableManager.queryAllFriendUser(curUser.getUserId());
    }

    @OnClick(R.id.view_updateSex)
    public void updateUserSex() {
        myTableManager.updateUserSex(curUser.getUserId(), "w");
    }
/*
    public List<User> queryAll() {
        List<User> list = myTableManager.findAll(User.class);
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
    }*/
}