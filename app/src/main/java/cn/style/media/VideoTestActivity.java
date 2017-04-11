package cn.style.media;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.style.constant.ConfigUtil;
import com.style.framework.R;
import com.style.lib.media.camera2video.CameraActivity;
import com.style.lib.media.video.PlayVideoActivity;
import com.style.utils.BitmapUtil;

import java.io.File;
import java.io.IOException;


public class VideoTestActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_RECORD_VIDEO = 1;
    private Button recordVideo;
    private Button uploadVideo;
    private Button downVideo;
    private Button playVideo;
    private String path = "/storage/emulated/0/FussenVideo/video1489041516574/1489041516574.mp4";
    private ImageView ivVideoPreview;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_test);
        /*if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/
        recordVideo = (Button) findViewById(R.id.btn_record);
        uploadVideo = (Button) findViewById(R.id.btn_upload);
        downVideo = (Button) findViewById(R.id.btn_down);
        playVideo = (Button) findViewById(R.id.btn_play);
        btnSave = (Button) findViewById(R.id.btn_save);

        ivVideoPreview = (ImageView) findViewById(R.id.iv_video_preview);

        recordVideo.setOnClickListener(this);
        uploadVideo.setOnClickListener(this);
        downVideo.setOnClickListener(this);
        playVideo.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == recordVideo) {
            startActivityForResult(new Intent(this, CameraActivity.class), REQUEST_CODE_RECORD_VIDEO);
        }

        if (view == playVideo) {
            Log.e("mainactivity", "path==" + path);
            Intent data = new Intent();
            data.putExtra("path", path);
            data.setClass(this, PlayVideoActivity.class);
            startActivity(data);
        }
        if (view == btnSave) {
            save();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case REQUEST_CODE_RECORD_VIDEO:
                    path = data.getStringExtra("videoPath");
                    setImageResource();
                    break;

            }
    }

    private void setImageResource() {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        Bitmap bitmap = retriever.getFrameAtTime(1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
        ivVideoPreview.setImageBitmap(bitmap);
        try {
            BitmapUtil.saveBitmap(ConfigUtil.DIR_CACHE+"/test.video.preview", bitmap, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void save() {
        File f = new File(path);
        if (f.exists())
            if (f.canWrite()) {
                File f2 = new File(f.getParentFile(), "dsff.mp4");
                f.renameTo(f2);//移动文件到新的路径
            }
    }
}
