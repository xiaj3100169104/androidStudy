package example.login.view;

import com.style.bean.User;

/**
 * Created by xiajun on 2017/9/7.
 */

public interface ILoginView extends IBaseView {
    void setUserView(User user);
    void skip2Main();

}
