package example.login.view;

/**
 * Created by xiajun on 2017/9/7.
 */

public interface ILoginView {
    void setUserName(String userName);
    void setPassword(String password);
    void skip2Main();
    void loginSuccess();
    void loginFailed();

}
