package example.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import example.activity.GreenDaoActivity;
import example.activity.GlideDealActivity;
import example.activity.TestRxActivity;

import com.style.base.BaseFragment;
import com.style.framework.R;
import com.style.framework.databinding.FragmentHome4Binding;


import example.ble.BLEActivity;
import example.ble.BlueToothActivity;
import example.media.socket.chat.SocketTestActivity;
import example.media.AudioRecordActivity;
import example.queue.QueueTestActivity;
import example.media.VideoTestActivity;
import example.webservice.WebServiceActivity;


public class HomeFragment4 extends BaseFragment {

    FragmentHome4Binding bd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bd = DataBindingUtil.inflate(inflater, R.layout.fragment_home_4, container, false);
        return bd.getRoot();

    }

    @Override
    protected void initData() {
        bd.setEvent(new EventListener());
        logE(TAG, "initData");

    }

    public class EventListener {

        public void testRX(View v) {
            skip(TestRxActivity.class);
        }

        public void testGreenDao(View v) {
            skip(GreenDaoActivity.class);
        }

        public void testGlide(View v) {
            skip(GlideDealActivity.class);
        }

        public void testAppCrash(View v) {
            String test = null;
            logE(TAG, test.toString());
        }

        public void skip46(View v) {
            skip(VideoTestActivity.class);
        }

        public void skip48(View v) {
            skip(AudioRecordActivity.class);
        }

        public void skip49(View v) {
            skip(SocketTestActivity.class);
        }

        public void skip491(View v) {
            skip(QueueTestActivity.class);
        }
        public void skip492(View v) {
            skip(BlueToothActivity.class);
        }
        public void skip493(View v) {
            skip(BLEActivity.class);
        }
        public void skip11(View v) {
            skip(WebServiceActivity.class);
        }

    }
}
