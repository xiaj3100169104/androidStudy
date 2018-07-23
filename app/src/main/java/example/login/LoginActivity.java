package example.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.Observable;
import android.view.View;

import com.style.base.BaseActivity;
import com.style.bean.User;
import com.style.framework.R;
import com.style.framework.databinding.ActivityLoginBinding;

import example.home.MainActivity;

public class LoginActivity extends BaseActivity {

    private long userId = 18;
    private ActivityLoginBinding bd;
    private LoginModel loginModel;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected boolean isGeneralTitleBar() {
        return false;
    }

    @Override
    public void initData() {
        bd = getBinding();
        loginModel = ViewModelProviders.of(this).get(LoginModel.class);
        loginModel.loginSucceed.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (loginModel.loginSucceed.get()) {
                    startActivity(new Intent(getContext(), MainActivity.class));
                    finish();
                }
            }
        });
        loginModel.user.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                setUserView(loginModel.user.get());
            }
        });
        loginModel.loginState.observe(this, aBoolean -> {
            if (aBoolean){
                startActivity(new Intent(getContext(), MainActivity.class));
                finish();
            }
        });
        loginModel.login();
    }

    public void login(View v) {
        String userId = bd.etAccount.getText().toString();
        String password = bd.etPassword.getText().toString();
        //mPresenter.login(userId, password);
    }

    public void setUserView(User user) {
        bd.etAccount.setText(user.userName);
        bd.etPassword.setText(user.password);
    }

    public void loginSuccess() {

    }

    public void loginFailed() {

    }
}