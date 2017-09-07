package example.login.presenter;

import com.style.bean.Friend;
import com.style.bean.User;
import com.style.db.user.UserDBManager;
import com.style.manager.AccountManager;

import java.util.List;

import example.login.model.ILoginModel;
import example.login.model.LoginModelImpl;
import example.login.view.ILoginView;

/**
 * Created by xiajun on 2017/9/7.
 */

public class LoginPresenterImpl implements ILoginPresenter {
    ILoginView loginView;
    ILoginModel loginModel;
    private User curUser;

    public LoginPresenterImpl(ILoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModelImpl();
    }

    @Override
    public void onActivityCreate() {
        curUser = loginModel.getCurrentUser();
        if (curUser != null) {
            if (loginModel.isAutoLogin()) {
                loginView.skip2Main();
                return;
            }
            loginView.setUserName(curUser.getUserId() + "");
            loginView.setPassword(curUser.getPassword());

        }
    }

    @Override
    public void login(String userName, String password) {
        loginModel.login(userName, password, new LoginModelImpl.LoginListener() {
            @Override
            public void success() {
                loginView.loginSuccess();
            }

            @Override
            public void failed() {
                loginView.loginFailed();
            }
        });

    }

    @Override
    public void onActivityDestroy() {

    }


}
