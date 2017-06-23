package example.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import butterknife.OnClick;
import xj.mqtt.bean.IMMessage;
import xj.mqtt.bean.TextMsg;
import xj.mqtt.db.MsgDBManager;
import xj.mqtt.manager.IMManagerImpl;
import xj.mqtt.service.MQTTService;


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

    @OnClick(R.id.view_1)
    public void exit() {
        //startListener();

    }

    protected Timer timer;
    private MyTimerTask task;
    public static final long DELAY_TIME = 5000;

    private void startListener() {
        task = new MyTimerTask();
        timer = new Timer(true);
        timer.schedule(task, 0, DELAY_TIME); //延时0ms后执行，1000ms执行一次
    }

    private void stopListener() {
        timer.cancel();
        task = null;
        timer = null;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
           /* if (msg.what == UPDATE) {
                updateCallingTime();
            }*/
            //timer.purge();
        }
    };

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            /*Message message = new Message();
            message.what = UPDATE;
            handler.sendMessage(message);*/

        }
    }
}
