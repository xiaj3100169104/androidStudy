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

import example.activity.AnimatorActivity;
import example.activity.DataBindingActivity;
import example.activity.GreenDaoActivity;
import example.activity.TestDBActivity;
import example.activity.TestRealmActivity;
import example.activity.TestRxActivity;
import example.address.AddressActivity;
import example.album.SelectLocalPictureActivity;

import com.style.base.BaseFragment;
import com.style.framework.R;

import aidl.IRemoteService;
import butterknife.OnClick;
import example.media.socket.chat.SocketTestActivity;
import example.media.AudioRecordActivity;
import example.activity.FileDownActivity;
import example.activity.MultiTypeActivity;
import example.activity.MyRadioGroupActivity;
import example.queue.QueueTestActivity;
import example.activity.UserAgreeActivity;
import example.media.VideoTestActivity;
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

    @OnClick(R.id.layout_item_415)
    public void skip415() {
        skip(TestRxActivity.class);
    }

    @OnClick(R.id.layout_item_416)
    public void skip416() {
        skip(GreenDaoActivity.class);
    }

    @OnClick(R.id.layout_item_417)
    public void skip417() {
        skip(TestRealmActivity.class);
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



}
