package example.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.style.address.AddressActivity;
import example.album.SelectLocalPictureActivity;

import com.style.base.BaseFragment;
import com.style.framework.R;

import aidl.IRemoteService;
import butterknife.OnClick;
import cn.style.media.socket.chat.SocketTestActivity;
import cn.style.media.AudioRecordActivity;
import example.activity.CircleProgressBarActivity;
import example.activity.FileDownActivity;
import example.activity.MultiTypeActivity;
import example.activity.MyRadioGroupActivity;
import example.queue.QueueTestActivity;
import example.activity.TestNotifyViewActivity;
import example.activity.TestProgressActivity;
import example.activity.UserAgreeActivity;
import cn.style.media.VideoTestActivity;
import example.activity.WebViewActivity;
import example.activity.WebViewAndJSActivity;
import example.activity.WheelActivity;

import example.ndk.JniTestActivity;


public class HomeFragment4 extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_home_4;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.layout_item_42)
    public void skip42() {
        skip(MultiTypeActivity.class);
    }

    @OnClick(R.id.layout_item_43)
    public void skip43() {
        skip(TestNotifyViewActivity.class);
    }

    @OnClick(R.id.layout_item_44)
    public void skip44() {
        skip(TestProgressActivity.class);
    }

    @OnClick(R.id.layout_item_45)
    public void skip45() {
        skip(CircleProgressBarActivity.class);
    }

    @OnClick(R.id.layout_item_46)
    public void skip46() {
        skip(VideoTestActivity.class);
    }

    @OnClick(R.id.layout_item_48)
    public void skip48() {
        skip(AudioRecordActivity.class);
    }

    @OnClick(R.id.layout_item_49)
    public void skip49() {
        skip(SocketTestActivity.class);
    }

    @OnClick(R.id.layout_item_491)
    public void skip491() {
        skip(QueueTestActivity.class);
    }


    @OnClick(R.id.btn_album)
    public void skip1() {
        skip(SelectLocalPictureActivity.class);
    }

    @OnClick(R.id.btn_address)
    public void skip2() {
        skip(AddressActivity.class);
    }

    @OnClick(R.id.btn_radio)
    public void skip3() {
        skip(MyRadioGroupActivity.class);
    }

    @OnClick(R.id.btn_wheel)
    public void skip4() {
        skip(WheelActivity.class);
    }

    @OnClick(R.id.btn_user_agree)
    public void skip5() {
        skip(UserAgreeActivity.class);
    }

    @OnClick(R.id.btn_file_down)
    public void skip6() {
        skip(FileDownActivity.class);
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
