package example.activity;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.style.base.BaseActivity;
import com.style.bean.Friend;
import com.style.bean.User;
import com.style.db.user.UserDBManager;
import com.style.framework.R;
import com.style.manager.AccountManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import example.home.MainActivity;
import example.login.presenter.ILoginPresenter;
import example.login.presenter.LoginPresenterImpl;
import example.login.view.ILoginView;

public class LoginActivity extends BaseActivity implements ILoginView {

    @Bind(R.id.login_progress)
    ProgressBar loginProgress;
    @Bind(R.id.et_account)
    AutoCompleteTextView etAccount;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.bt_sign_in)
    Button btSignIn;
    private long userId = 18;

    //@Inject
    ILoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_login;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
       /* Intent it = new Intent();
        it.setComponent(new ComponentName("com.style.framework",
                "xj.mqtt.service.MyService"));
        startService(it);*/


        presenter = new LoginPresenterImpl(this);
        presenter.onActivityCreate();

    }

    @OnClick(R.id.bt_sign_in)
    public void login() {

        String userId = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        presenter.login(userId, password);
    }

    @Override
    public void setUserName(String userName) {
        setText(etAccount, userName);
    }

    @Override
    public void setPassword(String password) {
        setText(etPassword, password);
    }

    @Override
    public void skip2Main() {
        skip(MainActivity.class);
        finish();
    }

    @Override
    public void loginSuccess() {
        skip2Main();
    }

    @Override
    public void loginFailed() {
        showToast("登录失败");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onActivityDestroy();
    }
}