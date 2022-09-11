package com.style.service.remote;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.IRemotePlayService;
import com.style.framework.R;
import com.style.framework.databinding.ActivityRemoteServiceBinding;
import com.style.utils.AppInfoUtil;

import org.jetbrains.annotations.Nullable;

public class RemotePlayActivity extends BaseTitleBarActivity {

    private IRemotePlayService remoteService;
    private ActivityRemoteServiceBinding bd;
    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        bd = ActivityRemoteServiceBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setTitleBarTitle("另起进程开启服务");
        conn();
    }

    public void start(View v) {
        try {
            remoteService.start(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void play(View v) {
        String channelNumber = AppInfoUtil.getAppMetaData(this);//获取app当前的渠道
        logE("channelNumber", channelNumber);
        bd.tvFlavor.setText(channelNumber);
    }

    public void stop(View v) {
        try {
            remoteService.stop("停止");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void conn() {
        Log.e(getTAG(), "begin bindService");
        Intent intent = new Intent("action.remote.play");
        intent.setPackage("com.style.framework");
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(getTAG(), "onServiceDisconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteService = IRemotePlayService.Stub.asInterface(service);
            int pid = 0;
            try {
                pid = remoteService.getPid();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            int currentPid = Process.myPid();
            Log.e(getTAG(), "currentPID: " + currentPid + "  remotePID: " + pid);

            Log.e(getTAG(), "onServiceConnected");
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
