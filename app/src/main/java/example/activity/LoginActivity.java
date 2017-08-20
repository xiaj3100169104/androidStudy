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

import butterknife.Bind;
import butterknife.OnClick;
import example.home.MainActivity;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.login_progress)
    ProgressBar loginProgress;
    @Bind(R.id.et_account)
    AutoCompleteTextView etAccount;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.bt_sign_in)
    Button btSignIn;
    private long userId = 18;
    private User curUser;

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

        curUser = AccountManager.getInstance().getCurrentUser();
        if (curUser != null) {
            skip(MainActivity.class);
            finish();
            return;

        }


    }

    @OnClick(R.id.bt_sign_in)
    public void login() {

        String userId = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        User user = new User(userId, password);
        AccountManager.getInstance().setCurrentUser(user);
        synData();
    }

    public void synData() {
        curUser = AccountManager.getInstance().getCurrentUser();

        List<User> friends = UserDBManager.getInstance().getAllMyFriend(curUser.getUserId());
        if (friends != null && friends.size() > 0) {
            skip(MainActivity.class);
            finish();
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

            skip2main();
        }

    }

    private void skip2main() {
        skip(MainActivity.class);
        finish();
    }


}