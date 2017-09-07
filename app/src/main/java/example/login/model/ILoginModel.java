package example.login.model;

import com.style.bean.User;

/**
 * Created by xiajun on 2017/9/7.
 */

public interface ILoginModel {

    User getCurrentUser();

    boolean isAutoLogin();

    void login(String userName, String password, LoginModelImpl.LoginListener listener);

}
