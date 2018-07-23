package example.music;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.style.base.BaseActivity;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.ActivityMusicListBinding;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;

import example.music.data.AudioLoader;
import example.music.data.MediaDataCallback;
import example.music.entity.MediaBean;
import example.music.entity.MediaFolder;


/**
 * Created by dmcBig on 2017/6/9.
 */

public class MusicListActivity extends BaseActivity implements MediaDataCallback {

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 5;
    ActivityMusicListBinding bd;
    private ArrayList<MediaBean> dataList;

    AudioAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_music_list;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("播放列表4");

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
            logE(TAG, "没有权限");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                logE(TAG, "上次拒绝");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                logE(TAG, "请求权限");
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
                logE(TAG, "权限允许");
                getMediaData();

            } else {
                logE(TAG, "权限拒绝");
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

    public void done(ArrayList<Media> selects) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(PickerConfig.EXTRA_RESULT, selects);
        setResult(PickerConfig.RESULT_CODE, intent);
        finish();
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

    public void bind(View v) {
        Intent i = new Intent(new Intent(this, PlayMusicService.class));
        bindService(i, mConnection, Context.BIND_AUTO_CREATE);

    }

    public void unbind(View v) {
        unbindService(mConnection);

    }

    public void stop(View v) {
        Intent i = new Intent(new Intent(this, PlayMusicService.class));
        stopService(i);
    }

    @Override
    protected void onDestroy() {
        //绑定了才调用此方法，不然会报错
        unbindService(mConnection);
        super.onDestroy();
    }

    PlayMusicService.MyBinder myBinder;
    private ServiceConnection mConnection = new ServiceConnection() {
        // 当与service的连接建立后被调用
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.e(TAG, "onServiceConnected");

            // Because we have bound to an explicit service that is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            //因为连接的service与当前activity在同一进程，所以可以强转并使用他
            myBinder = (PlayMusicService.MyBinder) service;

        }

        // 当与service的连接意外断开时被调用,调unbindservice不会触发此回调
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "onServiceDisconnected");

        }
    };

}
