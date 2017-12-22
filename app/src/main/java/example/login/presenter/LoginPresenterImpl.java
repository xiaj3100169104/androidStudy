package example.login.presenter;

import android.content.Context;

import com.style.bean.Friend;
import com.style.bean.User;
import com.style.db.user.UserDBManager;
import com.style.manager.AccountManager;

import java.util.List;

import example.activity.LoginActivity;
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

    public LoginPresenterImpl(String tag, ILoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModelImpl(tag);
    }



    @Override
    public void onActivityCreate() {
        curUser = loginModel.getCurrentUser();
        if (curUser != null) {
            if (loginModel.isAutoLogin()) {
                loginView.skip2Main();
                return;
            }
            loginView.setUserView(curUser);

        }
    }

    @Override
    public void login(String userName, String password) {
        loginModel.login(userName, password, new LoginModelImpl.LoginListener() {
            @Override
            public void success() {
                loginView.dismissProgressDialog();
                loginView.skip2Main();
            }

            @Override
            public void failed() {
                loginView.dismissProgressDialog();
            }
        });

    }

    @Override
    public void onActivityDestroy() {

    }


}
