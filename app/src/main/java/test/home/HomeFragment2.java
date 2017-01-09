package test.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.style.base.BaseFragment;
import com.style.bean.User;
import com.style.db.custom.UserDBManager;
import com.style.framework.R;

import butterknife.OnClick;


public class HomeFragment2 extends BaseFragment {

    private UserDBManager myTableManager;
    private User curUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_home_2;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {
        myTableManager = UserDBManager.getInstance();

    }

    @Override
    protected void onLazyLoad() {

    }


    @OnClick(R.id.view_get_all_user)
    public void get_all_user() {
        myTableManager.queryAllUser();
    }

    @OnClick(R.id.view_get_all_friend)
    public void get_all_ufriend() {
        myTableManager.queryAllFriend();
    }

    @OnClick(R.id.view_delete_friend)
    public void delete() {
        //myTableManager.deleteUser(curUser.getUserId());
        //curUser = null;
    }

    @OnClick(R.id.view_getFriend)
    public void getFriend() {
        myTableManager.queryAllFriend(curUser.getUserId());

    }

    @OnClick(R.id.view_getFriendUser)
    public void getFriendUser() {
        myTableManager.queryAllMyFriend(curUser.getUserId());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        myTableManager.closeDB();
    }

}
