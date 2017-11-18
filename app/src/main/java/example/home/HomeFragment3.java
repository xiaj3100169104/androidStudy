package example.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.style.base.BaseFragment;
import com.style.bean.Friend;
import com.style.bean.User;
import com.style.db.user.UserDBManager;
import com.style.framework.R;
import com.style.manager.AccountManager;
import com.style.utils.MyDateUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import aidl.IRemoteService;
import butterknife.OnClick;
import example.activity.AnimatorActivity;
import example.activity.DataBindingActivity;
import example.activity.TestDBActivity;
import example.activity.WebViewActivity;
import example.activity.WebViewAndJSActivity;
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

    @OnClick(R.id.btn_web_view_js)
    public void skip8() {
        skip(WebViewAndJSActivity.class);
    }

    @OnClick(R.id.btn_jni)
    public void skip9() {
        skip(JniTestActivity.class);
    }

    @OnClick(R.id.btn_aidl)
    public void skip10() {
        //skip(JniTestActivity.class);
        //getActivity().startService(new Intent(getContext(), AidlService.class));
        testAidl();
    }

    private void testAidl() {
        System.out.println("begin bindService");
        Intent intent = new Intent("duanqing.test.aidl");
        intent.setPackage("com.xiajun.voicephone");
        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private IRemoteService remoteService;

    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteService = IRemoteService.Stub.asInterface(service);
            try {
                int pid = remoteService.getPid();
                int currentPid = android.os.Process.myPid();
                System.out.println("currentPID: " + currentPid + "  remotePID: " + pid);
                remoteService.basicTypes(12, 1223, true, 12.2f, 12.3, "我们的爱，我明白");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            System.out.println("bind success! " + remoteService.toString());
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //如果没有进行过绑定操作，解绑会报错
        if (remoteService != null)
            getActivity().unbindService(conn);
    }
}
