package example.login.presenter;

/**
 * Created by xiajun on 2017/9/7.
 */

public interface ILoginPresenter {

    void onActivityCreate();

    void login(String userName, String password);

    void onActivityDestroy();
}
