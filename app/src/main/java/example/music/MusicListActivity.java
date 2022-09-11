package example.music;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;

import com.dmcbig.mediapicker.PickerConfig;
import com.style.base.BaseTitleBarActivity;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.ActivityMusicListBinding;
import com.style.view.diviver.DividerItemDecoration;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import example.music.data.AudioLoader;
import example.music.data.MediaDataCallback;
import example.music.entity.MediaBean;
import example.music.entity.MediaFolder;

import com.style.service.music.PlayMusicService;


public class MusicListActivity extends BaseTitleBarActivity implements MediaDataCallback {

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 5;
    ActivityMusicListBinding bd;
    private ArrayList<MediaBean> dataList;

    AudioAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        bd = ActivityMusicListBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setTitleBarTitle("播放列表4");

        dataList = new ArrayList<>();
        adapter = new AudioAdapter(this, dataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        bd.recyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        bd.recyclerView.setHasFixedSize(true);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(this));
        bd.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<MediaBean>() {
            @Override
            public void onItemClick(int position, MediaBean data) {
                myBinder.play(data);

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            logE(getTAG(), "没有权限");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                logE(getTAG(), "上次拒绝");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                logE(getTAG(), "请求权限");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
            getMediaData();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                logE(getTAG(), "权限允许");
                getMediaData();

            } else {
                logE(getTAG(), "权限拒绝");
                // Permission Denied
                showToast("Permission Denied");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getMediaData() {
        getLoaderManager().initLoader(101, null, new AudioLoader(this, this));

    }

    @Override
    public void onData(ArrayList<MediaFolder> list) {
        for (MediaFolder f : list) {
            if (f.getMedias() != null && f.getMedias().size() > 0) {
                dataList.addAll(f.getMedias());
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void start(View v) {
        Intent i = new Intent(new Intent(this, PlayMusicService.class));
        startService(i);

    }

    public void stop(View v) {
        Intent i = new Intent(new Intent(this, PlayMusicService.class));
        stopService(i);
    }

    Boolean isBind = false;

    public void bind(View v) {
        Intent i = new Intent(new Intent(this, PlayMusicService.class));
        bindService(i, mConnection, Context.BIND_AUTO_CREATE);
        isBind = true;
    }

    public void unbind(View v) {
        unbind2();
    }

    public void unbind2() {
        if (isBind) {
            try {
                //绑定了才调用此方法，不然会报错
                unbindService(mConnection);
                isBind = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        unbind2();
        super.onDestroy();
    }

    PlayMusicService.MyBinder myBinder;
    private ServiceConnection mConnection = new ServiceConnection() {
        // 当与service的连接建立后被调用
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.e(getTAG(), "onServiceConnected");

            // Because we have bound to an explicit service that is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            //因为连接的service与当前activity在同一进程，所以可以强转并使用他
            myBinder = (PlayMusicService.MyBinder) service;

        }

        // 当与service的连接意外断开时被调用,调unbindservice不会触发此回调
        public void onServiceDisconnected(ComponentName className) {
            Log.e(getTAG(), "onServiceDisconnected");

        }
    };

}
