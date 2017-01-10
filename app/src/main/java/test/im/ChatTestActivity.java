package test.im;

import android.os.Bundle;

import com.style.base.BaseActivity;
import com.style.bean.Friend;
import com.style.bean.IMsg;
import com.style.db.base.MsgDBManager;
import com.style.framework.R;

import java.util.List;

import butterknife.OnClick;

import com.style.bean.User;
import com.style.manager.AccountManager;
import com.style.utils.MyDateUtil;

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
        MsgDBManager.getInstance().deleteOneFriendMsg(curUser.getUserId(), 1);
    }

    @OnClick(R.id.view_page_load)
    public void getPageLoad() {
       // MsgDBManager.getInstance().queryFriendMsgByPage();
    }

    @OnClick(R.id.view_send)
    public void send() {
        IMsg o = new IMsg();
        o.setMsgId(System.currentTimeMillis());
        o.setSenderId(8);
        o.setReceiverId(1);
        o.setState(0);
        o.setCreateTime(System.currentTimeMillis());
        o.setContent(MyDateUtil.longToString(System.currentTimeMillis(), MyDateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));
        MsgDBManager.getInstance().insertMsg(o);
    }

}