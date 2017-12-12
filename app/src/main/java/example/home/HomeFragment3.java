package example.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.style.base.BaseFragment;
import com.style.bean.User;
import com.style.framework.R;
import com.style.manager.AccountManager;

import butterknife.OnClick;
import example.activity.AidlActivity;
import example.activity.AnimatorActivity;
import example.activity.DataBindingActivity;
import example.activity.UserAgreeActivity;
import example.activity.MsgToSubActivity;
import example.activity.TestDBActivity;
import example.activity.WebServiceActivity;
import example.activity.WebViewActivity;
import example.activity.WebViewAndJSActivity;
import example.activity.WebViewFeedbackActivity;
import example.ndk.JniTestActivity;


public class HomeFragment3 extends BaseFragment {
    private User curUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_home_3;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {
        curUser = AccountManager.getInstance().getCurrentUser();
    }

    @OnClick(R.id.layout_item_417)
    public void skip417() {
        skip(MsgToSubActivity.class);
    }

    @OnClick(R.id.layout_item_418)
    public void skip418() {
        skip(TestDBActivity.class);
    }

    @OnClick(R.id.layout_item_419)
    public void skip419() {
        skip(DataBindingActivity.class);
    }


    @OnClick(R.id.layout_item_414)
    public void skip414() {
        skip(AnimatorActivity.class);
    }

    @OnClick(R.id.btn_web_view)
    public void skip7() {
        skip(WebViewActivity.class);
    }
    @OnClick(R.id.btn_web_view2)
    public void skip71() {
        skip(UserAgreeActivity.class);
    }

    @OnClick(R.id.btn_web_view_js)
    public void skip8() {
        skip(WebViewAndJSActivity.class);
    }
    @OnClick(R.id.btn_feedback)
    public void skip81() {
        skip(WebViewFeedbackActivity.class);
    }

    @OnClick(R.id.btn_jni)
    public void skip9() {
        skip(JniTestActivity.class);
    }

    @OnClick(R.id.btn_aidl)
    public void skip10() {
        skip(AidlActivity.class);
    }

    @OnClick(R.id.btn_webservice)
    public void skip11() {
        skip(WebServiceActivity.class);
    }

}
