package example.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.view.View;

import com.style.base.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityAidlBinding;

import org.jetbrains.annotations.Nullable;

import aidl.IRemoteService;

public class AidlActivity extends BaseDefaultTitleBarActivity {

    private IRemoteService remoteService;
    private ActivityAidlBinding bd;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_aidl);
        bd = getBinding();
        conn();
        bd.tvOpenActivityWithMimeType.setOnClickListener(v -> openActivityWithMimeType());
        bd.tvOpenOtherAppActivity.setOnClickListener(v -> openOtherAppActivity());
        bd.tvLaunch.setOnClickListener(v -> launchOtherApp());
        bd.tvSendToOtherApp.setOnClickListener(v -> sendMsgToOtherApp());
    }

    private void openActivityWithMimeType() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("other", "other");
        intent.setDataAndType(Uri.parse("content://www.google.com"), "application/pdf");
        startActivity(intent);
    }

    public void openOtherAppActivity() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setComponent(new ComponentName("com.wifidemo.example.xiajun.myapplication.default", "com.example.WebViewActivity"));
        intent.putExtra("other", "other");
        intent.setData(Uri.parse("http://www.google.com"));
        startActivity(intent);
    }

    /**
     * 两种方式都可以，第二种貌似以singleInstance打开。
     * 注：包名指applicationId
     */
    public void launchOtherApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName("com.wifidemo.example.xiajun.myapplication.default", "com.example.MainActivity");
        startActivity(intent);
        /*Intent intent = getContext().getPackageManager().getLaunchIntentForPackage("com.wifidemo.example.xiajun.myapplication");
        startActivity(intent);*/
    }

    public void sendMsgToOtherApp() {
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
        //如果没有进行过绑定操作，解绑会报错,绑定和解绑都要一一对应。
        if (remoteService != null)
            unbindService(conn);
    }
}
