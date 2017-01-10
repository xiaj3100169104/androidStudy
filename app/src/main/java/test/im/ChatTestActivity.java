package test.im;

import android.os.Bundle;

import com.style.base.BaseActivity;
import com.style.bean.Friend;
import com.style.framework.R;

import java.util.List;

import butterknife.OnClick;

import com.style.bean.User;
import com.style.manager.AccountManager;

public class ChatTestActivity extends BaseActivity {

    private User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_chat_test;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        curUser = AccountManager.getInstance().getCurrentUser();
    }

    @OnClick(R.id.view_clear)
    public void getClear() {

    }

    @OnClick(R.id.view_page_load)
    public void getPageLoad() {

    }

    @OnClick(R.id.view_send)
    public void delete() {

    }

}