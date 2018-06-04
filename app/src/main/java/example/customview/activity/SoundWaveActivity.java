package example.customview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.style.framework.R;
import com.style.view.SoundWaveView;

public class SoundWaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_wave);
        final SoundWaveView sineCurve = (SoundWaveView) findViewById(R.id.custom_view2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sineCurve.startAnimation();

            }
        },1000);
    }
}
