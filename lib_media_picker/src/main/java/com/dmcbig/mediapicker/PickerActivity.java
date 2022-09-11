package com.dmcbig.mediapicker;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmcbig.mediapicker.adapter.FolderAdapter;
import com.dmcbig.mediapicker.adapter.MediaGridAdapter;
import com.dmcbig.mediapicker.adapter.SpacingDecoration;
import com.dmcbig.mediapicker.data.DataCallback;
import com.dmcbig.mediapicker.data.ImageLoader;
import com.dmcbig.mediapicker.data.MediaLoader;
import com.dmcbig.mediapicker.data.VideoLoader;
import com.dmcbig.mediapicker.entity.Folder;
import com.dmcbig.mediapicker.entity.Media;
import com.dmcbig.mediapicker.utils.ScreenUtils;

import java.util.ArrayList;


/**
 * Created by dmcBig on 2017/6/9.
 */

public class PickerActivity extends AppCompatActivity implements DataCallback, View.OnClickListener {

    Intent argsIntent;
    RecyclerView recyclerView;
    Button done, category_btn, preview;
    MediaGridAdapter gridAdapter;
    ListPopupWindow mFolderPopupWindow;
    private FolderAdapter mFolderAdapter;
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        argsIntent = getIntent();
        setContentView(R.layout.main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        findViewById(R.id.btn_back).setOnClickListener(this);
        setTitleBar();
        done = (Button) findViewById(R.id.done);
        category_btn = (Button) findViewById(R.id.category_btn);
        preview = (Button) findViewById(R.id.preview);
        done.setOnClickListener(this);
        category_btn.setOnClickListener(this);
        preview.setOnClickListener(this);
        //get view end
        createAdapter();
        createFolderAdapter();
        getMediaData();
    }

    public void setTitleBar() {
        int type = argsIntent.getIntExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE_VIDEO);
        if (type == PickerConfig.PICKER_IMAGE_VIDEO) {
            ((TextView) findViewById(R.id.bar_title)).setText(getString(R.string.select_title));
        } else if (type == PickerConfig.PICKER_IMAGE) {
            ((TextView) findViewById(R.id.bar_title)).setText(getString(R.string.select_image_title));
        } else if (type == PickerConfig.PICKER_VIDEO) {
            ((TextView) findViewById(R.id.bar_title)).setText(getString(R.string.select_video_title));
        }
    }

    void createAdapter() {
        //创建默认的线性LayoutManager
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, PickerConfig.GridSpanCount);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SpacingDecoration(PickerConfig.GridSpanCount, PickerConfig.GridSpace));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        ArrayList<Media> medias = new ArrayList<>();
        ArrayList<Media> select = argsIntent.getParcelableArrayListExtra(PickerConfig.DEFAULT_SELECTED_LIST);
        int maxSelect = argsIntent.getIntExtra(PickerConfig.MAX_SELECT_COUNT, PickerConfig.DEFAULT_SELECTED_MAX_COUNT);
        long maxSize = argsIntent.getLongExtra(PickerConfig.MAX_SELECT_SIZE, PickerConfig.DEFAULT_SELECTED_MAX_SIZE);
        gridAdapter = new MediaGridAdapter(medias, this, select, maxSelect, maxSize);
        recyclerView.setAdapter(gridAdapter);
    }

    void createFolderAdapter() {
        ArrayList<Folder> folders = new ArrayList<>();
        mFolderAdapter = new FolderAdapter(folders, this);
        mFolderPopupWindow = new ListPopupWindow(this);
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setHeight((int) (ScreenUtils.getScreenHeight(this) * 0.7));
        mFolderPopupWindow.setAnchorView(findViewById(R.id.footer));
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFolderAdapter.setSelectIndex(position);
                category_btn.setText(mFolderAdapter.getItem(position).name);
                gridAdapter.updateAdapter(mFolderAdapter.getSelectMedias());
                mFolderPopupWindow.dismiss();
            }
        });
    }

    void getMediaData() {
        int type = argsIntent.getIntExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE_VIDEO);
        if (type == PickerConfig.PICKER_IMAGE_VIDEO) {
            getLoaderManager().initLoader(type, null, new MediaLoader(this, this));
        } else if (type == PickerConfig.PICKER_IMAGE) {
            getLoaderManager().initLoader(type, null, new ImageLoader(this, this));
        } else if (type == PickerConfig.PICKER_VIDEO) {
            getLoaderManager().initLoader(type, null, new VideoLoader(this, this));
        }
    }

    @Override
    public void onData(ArrayList<Folder> list) {
        setView(list);
        category_btn.setText(list.get(0).name);
        mFolderAdapter.updateAdapter(list);
    }

    void setView(ArrayList<Folder> list) {
        gridAdapter.updateAdapter(list.get(0).getMedias());
        setButtonText();
        gridAdapter.setOnItemClickListener(new MediaGridAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Media data, ArrayList<Media> selectMedias) {
                setButtonText();
            }
        });
    }

    void setButtonText() {
        int max = argsIntent.getIntExtra(PickerConfig.MAX_SELECT_COUNT, PickerConfig.DEFAULT_SELECTED_MAX_COUNT);
        done.setText(getString(R.string.done) + "(" + gridAdapter.getSelectMedias().size() + "/" + max + ")");
        preview.setText(getString(R.string.preview) + "(" + gridAdapter.getSelectMedias().size() + ")");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            finish();
        } else if (id == R.id.category_btn) {
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            } else {
                mFolderPopupWindow.show();
            }
        } else if (id == R.id.done) {
            done(gridAdapter.getSelectMedias());
        } else if (id == R.id.preview) {
            if (gridAdapter.getSelectMedias().size() <= 0) {
                Toast.makeText(this, getString(R.string.select_null), Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, PreviewActivity.class);
            intent.putExtra(PickerConfig.MAX_SELECT_COUNT, argsIntent.getIntExtra(PickerConfig.MAX_SELECT_COUNT, PickerConfig.DEFAULT_SELECTED_MAX_COUNT));
            intent.putExtra(PickerConfig.PRE_RAW_LIST, gridAdapter.getSelectMedias());
            this.startActivityForResult(intent, PickerConfig.CODE_TAKE_ALBUM);
        }
    }

    public void done(ArrayList<Media> selects) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(PickerConfig.EXTRA_RESULT, selects);
        setResult(PickerConfig.RESULT_CODE, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        Glide.get(this).clearMemory();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            ArrayList<Media> selects = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            if (resultCode == PickerConfig.RESULT_UPDATE_CODE) {
                gridAdapter.updateSelectAdapter(selects);
                setButtonText();
            } else if (resultCode == PickerConfig.RESULT_CODE) {
                done(selects);
            }
        }
    }
}
