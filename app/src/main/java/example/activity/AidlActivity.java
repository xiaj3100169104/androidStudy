package example.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;

import aidl.IRemoteService;
import butterknife.Bind;
import butterknife.OnClick;

public class AidlActivity extends BaseToolBarActivity {


    @Bind(R.id.tv_send)
    TextView tvSend;
    @Bind(R.id.tv_launch)
    TextView tvLaunch;

    private IRemoteService remoteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_aidl;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        conn();
    }

    @OnClick(R.id.tv_launch)
    public void launch() {
        //没有这个app没有任何反应
        /*Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName("com.xiajun.voicephone", "com.xiajun.voicephone.MainActivity"));
        startActivity(intent);*/
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.xiajun.voicephone");
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_send)
    public void send() {
        try {
            remoteService.basicTypes(12, 1223, true, 12.2f, 12.3, "来自其他应用的消息");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void conn() {
        System.out.println("begin bindService");
        Intent intent = new Intent("duanqing.test.aidl");
        intent.setPackage("com.xiajun.voicephone");
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteService = IRemoteService.Stub.asInterface(service);
            try {
                int pid = remoteService.getPid();
                int currentPid = Process.myPid();
                System.out.println("currentPID: " + currentPid + "  remotePID: " + pid);
                remoteService.basicTypes(12, 1223, true, 12.2f, 12.3, "来自其他应用的连接");
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
            unbindService(conn);
    }
}
