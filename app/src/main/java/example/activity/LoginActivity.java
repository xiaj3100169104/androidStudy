package example.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.style.base.BaseActivity;
import com.style.bean.User;
import com.style.framework.R;
import com.style.framework.databinding.ActivityLoginBinding;

import example.home.MainActivity;
import example.login.presenter.ILoginPresenter;
import example.login.presenter.LoginPresenterImpl;
import example.login.view.ILoginView;

public class LoginActivity extends BaseActivity implements ILoginView {

    private long userId = 18;

    //@Inject
    ILoginPresenter presenter;
    private ActivityLoginBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_login);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        presenter = new LoginPresenterImpl(TAG, this);
        presenter.onActivityCreate();

    }

    public void login(View v) {

        String userId = bd.etAccount.getText().toString();
        String password = bd.etPassword.getText().toString();
        presenter.login(userId, password);
    }

    @Override
    public void setUserView(User user) {
        bd.etAccount.setText(user.userName);
        bd.etPassword.setText(user.password);
    }

    @Override
    public void skip2Main() {
        skip(MainActivity.class);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onActivityDestroy();
    }
}