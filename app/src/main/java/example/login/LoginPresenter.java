package example.login;


import com.style.base.BaseActivityPresenter;
import com.style.bean.User;
import com.style.data.prefs.AccountManager;
import com.style.data.net.response.BaseResult;

import example.newwork.response.LoginBean;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class LoginPresenter extends BaseActivityPresenter<LoginActivity> {

    public LoginPresenter(LoginActivity mActivity) {
        super(mActivity);
    }

    public void getLoginUser() {
        String a = getAccountManager().getCurrentAccount();
        getActivity().setUserView(getAccountManager().getUser(a));
    }

    public void login(String userName, String password) {
        User user = new User(userName, password);
        AccountManager.getInstance().setCurrentUser(user);
        synData();
        Disposable d = getHttpApi().login(userName, password).subscribe(new Consumer<BaseResult<LoginBean>>() {
            @Override
            public void accept(BaseResult<LoginBean> loginBeanBaseResult) throws Exception {
                getActivity().loginSuccess();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                getActivity().loginFailed();
            }
        });
        addTask(d);

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
