package example.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.BaseToolBarActivity;
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

public class MusicListActivity extends BaseToolBarActivity implements MediaDataCallback {

    ActivityMusicListBinding bd;
    private ArrayList<MediaBean> dataList;

    AudioAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_music_list);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("播放列表");

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
        getMediaData();
        Intent i = new Intent(new Intent(this, PlayMusicService.class));
        bindService(i, mConnection, Context.BIND_AUTO_CREATE);

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
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void start(View v) {
        myBinder.start(0);
    }

    public void play(View v) {
    }

    public void stop(View v) {
        myBinder.stop("停止");
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

        // 当与service的连接意外断开时被调用
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "onServiceDisconnected");

        }
    };

}
