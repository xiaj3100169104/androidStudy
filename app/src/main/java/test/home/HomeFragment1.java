package test.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.style.album.SelectLocalPictureActivity;
import com.style.base.BaseFragment;
import com.style.framework.R;
import com.style.newwork.common.HttpAction;
import com.style.newwork.common.NetDataBeanCallback;
import com.style.newwork.response.LoginBean;
import com.style.utils.CommonUtil;

import butterknife.OnClick;


public class HomeFragment1 extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_home_1;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onLazyLoad() {

    }

    @OnClick(R.id.btn_album)
    public void skip1() {
        //skip(SelectLocalPictureActivity.class);
        login("18202823096","123456");
    }

    private void login(String userName, final String password) {

        showProgressDialog("正在登录。。。");
        //UserRequest.login(userName, password, loginCallBack);
        HttpAction.login(userName, password, new NetDataBeanCallback<LoginBean>(LoginBean.class) {
            @Override
            protected void onCodeSuccess(LoginBean data) {
                dismissProgressDialog();
                /*AccountManager.getInstance().setUser(data.userBean);
                AccountManager.getInstance().setToken(data.token);
                AccountManager.getInstance().setUserPassWord(password);
                skip(MainActivity.class);
                finish();*/
            }

            @Override
            protected void onCodeFailure(String msg) {
                //dismissProgressDialog();
                showToast(msg);
            }
        });
    }
}
