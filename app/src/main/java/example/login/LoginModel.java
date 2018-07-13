package example.login;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.style.bean.User;
import com.style.data.net.bean.UserInfo;

/**
 * Created by xiajun on 2018/7/13.
 */

public class LoginModel extends ViewModel {

    ObservableBoolean loginSucceed = new ObservableBoolean(false);
    ObservableField<User> user = new ObservableField<>();

    public void login() {
        User u = new User("sfsf", "fasfgasfg");
        user.set(u);
        u.setPassword("123456");
        loginSucceed.set(true);
    }
}
