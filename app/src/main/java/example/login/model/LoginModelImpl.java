package example.login.model;

import com.style.bean.Friend;
import com.style.bean.User;
import com.style.db.user.UserDBManager;
import com.style.manager.AccountManager;

import java.util.List;

/**
 * Created by xiajun on 2017/9/7.
 */

public class LoginModelImpl implements ILoginModel {
    private User curUser;

    @Override
    public User getCurrentUser() {
        curUser = AccountManager.getInstance().getCurrentUser();
        return curUser;
    }

    @Override
    public boolean isAutoLogin() {
        return AccountManager.getInstance().getIsAutoLogin(curUser.getUserId());
    }

    @Override
    public void login(String userName, String password, LoginListener listener) {
        User user = new User(userName, password);
        AccountManager.getInstance().setCurrentUser(user);
        synData(listener);

    }

    public void synData(LoginListener listener) {
        curUser = AccountManager.getInstance().getCurrentUser();

        List<User> friends = UserDBManager.getInstance().getAllMyFriend(curUser.getUserId());
        if (friends != null && friends.size() > 0) {
            listener.success();
        } else {
            for (int i = 2; i < 10; i++) {
                User user = new User(i + "", "123456");
                UserDBManager.getInstance().insertUser(user);
            }

            for (int i = 2; i < 5; i++) {
                Friend bean = new Friend();
                bean.setFriendId(i + "");
                bean.setOwnerId(curUser.getUserId());
                bean.setMark("朋友" + i);
                UserDBManager.getInstance().insertFriend(bean);
            }
            listener.success();

        }
    }

    public static class LoginListener {
        public void success() {

        }

        public void failed() {
        }
    }
}
