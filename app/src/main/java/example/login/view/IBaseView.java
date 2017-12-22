package example.login.view;

/**
 * Created by xiajun on 2017/12/21.
 */

public interface IBaseView {
    void showProgressDialog();
    void showProgressDialog(String msg);
    void dismissProgressDialog();
    void showToast(String msg);
    void showToast(int resId);
}
