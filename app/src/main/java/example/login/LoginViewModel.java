package example.login;

import android.arch.lifecycle.ViewModel;

import com.style.bean.Friend;
import com.style.bean.User;
import com.style.db.user.UserDBManager;
import com.style.manager.AccountManager;
import com.style.net.core2.BaseObserver;
import com.style.net.core2.BaseResult;
import com.style.net.core2.RetrofitImpl;

import java.util.List;

import example.newwork.response.LoginBean;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by xiajun on 2018/4/8.
 */

public class LoginViewModel extends ViewModel {

    public LoginViewModel() {
    }

    public User getLoginUser() {
        String a = AccountManager.getInstance().getCurrentAccount();
        return AccountManager.getInstance().getUser(a);
    }

    public void login(String userName, String password) {
        User user = new User(userName, password);
        AccountManager.getInstance().setCurrentUser(user);
        synData();
        RetrofitImpl.getInstance().login(userName, password).subscribe(new Consumer<BaseResult<LoginBean>>() {
            @Override
            public void accept(BaseResult<LoginBean> loginBeanBaseResult) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        }, new Action() {
            @Override
            public void run() throws Exception {

            }
        }, new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {

            }
        });

    }

    public void synData() {
        /*curUser = AccountManager.getInstance().getCurrentUser();
        List<User> friends = UserDBManager.getInstance().getAllMyFriend(curUser.getUserId());
        if (friends != null && friends.size() > 0) {
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

        }*/
    }
}
