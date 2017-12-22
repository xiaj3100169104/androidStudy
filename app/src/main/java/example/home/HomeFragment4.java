package example.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import example.activity.GreenDaoActivity;
import example.activity.TestRealmActivity;
import example.activity.TestRxActivity;

import com.style.base.BaseFragment;
import com.style.framework.R;
import com.style.net.core2.BaseObserver;
import com.style.net.core2.KuaiDiModel;
import com.style.net.core2.RetrofitImpl;

import butterknife.OnClick;
import example.media.socket.chat.SocketTestActivity;
import example.media.AudioRecordActivity;
import example.queue.QueueTestActivity;
import example.media.VideoTestActivity;


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

    @OnClick(R.id.layout_item_418)
    public void skip418() {
        String test = null;
        logE(TAG, test.toString());
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
