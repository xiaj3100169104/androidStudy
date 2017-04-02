package test.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.style.framework.R;
import com.style.lib.media.audio.VoicePlayManager;
import com.style.lib.media.audio.VoiceRecordManager;

public class AudioRecordActivity extends AppCompatActivity {
    private VoiceRecordManager instance;

    private Button btnStart;
    private Button btnStop;
    private Button btnPlay;
    private Button btnStopPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnStopPlay = (Button) findViewById(R.id.btn_stop_play);

        instance = VoiceRecordManager.getInstance();
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
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
        btnStopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlay();
            }
        });
    }

    private void stopPlay() {
        VoicePlayManager.getInstance().stop();
    }

    private void play() {
        VoicePlayManager.getInstance().play("audio.pcm");
    }

    private void stop() {
        instance.stopRecording();
    }

    private void start() {
        instance.startRecord();
    }

}
