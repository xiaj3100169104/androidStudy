package example.login;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.style.base.BaseActivity;
import com.style.bean.User;
import com.style.framework.R;
import com.style.framework.databinding.ActivityLoginBinding;

import example.home.MainActivity;

public class LoginActivity extends BaseActivity {

    private long userId = 18;
    private ActivityLoginBinding bd;
    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_login);
        super.setContentView(bd.getRoot());
    }

    @Override
    public void initData() {
        mPresenter = new LoginPresenter(this);
        mPresenter.getLoginUser();
    }


    public void login(View v) {
        String userId = bd.etAccount.getText().toString();
        String password = bd.etPassword.getText().toString();
        mPresenter.login(userId, password);
    }

    public void setUserView(User user) {
        bd.etAccount.setText(user.userName);
        bd.etPassword.setText(user.password);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    public void loginSuccess() {

    }

    public void loginFailed() {

    }
}