package example.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.style.address.AddressActivity;
import com.style.album.SelectLocalPictureActivity;
import com.style.base.BaseFragment;
import com.style.framework.R;

import butterknife.OnClick;
import cn.style.socket.chat.SocketTestActivity;
import cn.style.media.AudioRecordActivity;
import example.activity.CircleProgressBarActivity;
import example.activity.FileDownActivity;
import example.activity.MultiTypeActivity;
import example.activity.MyRadioGroupActivity;
import example.activity.TestNotifyViewActivity;
import example.activity.TestProgressActivity;
import example.activity.UserAgreeActivity;
import cn.style.media.VideoTestActivity;
import example.activity.WebViewActivity;
import example.activity.WebViewAndJSActivity;
import example.activity.WheelActivity;
import xiajun.example.ndk.JniTestActivity;


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
}
