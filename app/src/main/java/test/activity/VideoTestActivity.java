package test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.style.framework.R;
import com.style.lib.media.camera2video.CameraActivity;
import com.style.lib.media.video.PlayVideoActivity;

import java.io.File;


public class VideoTestActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_RECORD_VIDEO = 1;
    private Button recordVideo;
    private Button uploadVideo;
    private Button downVideo;
    private Button playVideo;
    private String path = "/storage/emulated/0/FussenVideo/video1489041516574/1489041516574.mp4";
    private File file;

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
        recordVideo.setOnClickListener(this);
        uploadVideo.setOnClickListener(this);
        downVideo.setOnClickListener(this);
        playVideo.setOnClickListener(this);
        file = new File(path);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case REQUEST_CODE_RECORD_VIDEO:
                    path = data.getStringExtra("videoPath");
                    file = new File(path);
                    break;

            }
    }
}
