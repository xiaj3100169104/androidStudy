package cn.style.media;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.style.media.audio.VoicePlayManager;
import cn.style.media.audio.VoiceRecordManager;
import cn.style.media.audio.VoiceRecorder;
import com.style.framework.R;

public class AudioRecordActivity extends AppCompatActivity {

    private Button btnStart;
    private Button btnStop;
    private Button btnPlay;
    private Button btnRelease;
    private Button btnRecordAndPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        btnStart = (Button) findViewById(R.id.btn_record);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnRelease = (Button) findViewById(R.id.btn_release);
        btnRecordAndPlay = (Button) findViewById(R.id.btn_record_and_play);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecord();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        btnRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                release();
            }
        });

        btnRecordAndPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordAndPlay();
            }
        });

    }

    private void play() {
        VoicePlayManager.getInstance().play("audio.pcm");
    }

    private void stop() {
        VoiceRecordManager.getInstance().stopRecording();
        VoicePlayManager.getInstance().stop();
        VoiceRecorder.getInstance().stopRecording();
    }

    private void startRecord() {
        VoiceRecordManager.getInstance().startRecord();
    }

    private void recordAndPlay() {
        VoiceRecorder.getInstance().startRecord();
    }

    private void release() {
        VoiceRecordManager.getInstance().release();
        VoicePlayManager.getInstance().release();
        VoiceRecorder.getInstance().release();    }

}
