package test.activity;

import android.os.Bundle;

import com.style.base.BaseActivity;
import com.style.bean.Friend;
import com.style.bean.User;
import com.style.db.user.UserDBManager;
import com.style.framework.R;
import com.style.manager.AccountManager;

import java.util.List;

import test.home.MainActivity;

public class LoginActivity extends BaseActivity {

    private long userId = 18;
    private User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_login;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        login();
    }

    public void login() {

        curUser = AccountManager.getInstance().getCurrentUser();
        if (curUser == null) {
            User user = new User(userId, "18202823096", "123456", "夏军", "1234567890");
            AccountManager.getInstance().setCurrentUser(user);
        }
        curUser = AccountManager.getInstance().getCurrentUser();

        synData();
    }

    public void synData() {

        List<User> friends = UserDBManager.getInstance().getAllMyFriend(curUser.getUserId());
        if (friends != null && friends.size() > 0) {
            skip(MainActivity.class);
            finish();
        } else {
            for (int i = 1; i < userId; i++) {
                User user = new User(i, "182028200" + i, "123456", "用户" + i, null);
                UserDBManager.getInstance().insertUser(user);
            }

            for (int i = 1; i < 18; i++) {
                Friend bean = new Friend();
                bean.setFriendId(i);
                bean.setOwnerId(curUser.getUserId());
                bean.setMark("朋友" + i);
                UserDBManager.getInstance().insertFriend(bean);
            }

            skip(MainActivity.class);
            finish();
        }

    }
}