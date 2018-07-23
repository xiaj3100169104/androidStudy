package example.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.style.app.MyApp;
import com.style.base.BaseAndroidViewModel;
import com.style.bean.User;
import com.style.data.net.bean.UserInfo;
import com.style.data.net.response.BaseResult;
import com.style.data.prefs.AccountManager;

import example.newwork.response.LoginBean;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by xiajun on 2018/7/13.
 */

public class LoginModel extends BaseAndroidViewModel {

    ObservableBoolean loginSucceed = new ObservableBoolean(false);
    ObservableField<User> user = new ObservableField<>();
    MutableLiveData<Boolean> loginState = new MutableLiveData<>();

    public LoginModel(@NonNull Application application) {
        super(application);
    }

    public void login() {
        User u = new User("sfsf", "fasfgasfg");
        user.set(u);
        u.setPassword("123456");
        //loginSucceed.set(true);
        loginState.setValue(true);
        MyApp app = getApplication();
        app.initRefreshView();
    }

    public void getLoginUser() {
        String a = getPreferences().getCurrentAccount();
    }

    public void login(String userName, String password) {
        User user = new User(userName, password);
        AccountManager.getInstance().setCurrentUser(user);
        synData();
        Disposable d = getHttpApi().login(userName, password).subscribe(new Consumer<BaseResult<LoginBean>>() {
            @Override
            public void accept(BaseResult<LoginBean> loginBeanBaseResult) throws Exception {
                //getActivity().loginSuccess();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //getActivity().loginFailed();
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
