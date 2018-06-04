package example.customview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.style.framework.R;

public class CustomViewMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_view_main);
        findViewById(R.id.bt_temperature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(TemperatureActivity.class);
            }
        });
        findViewById(R.id.bt_curve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(CurveActivity2.class);
            }
        });
        findViewById(R.id.bt_notify_point).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(CustomNotifyViewActivity.class);
            }
        });
        findViewById(R.id.bt_progress1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(HorizontalProgressActivity.class);
            }
        });
        findViewById(R.id.bt_progress2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(CircleProgressBarActivity.class);
            }
        });
        findViewById(R.id.bt_write_word).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(WriteWordActivity.class);
            }
        });
        findViewById(R.id.bt_water_polo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(WaterPoloActivity.class);
            }
        });
        findViewById(R.id.bt_sound_wave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(SoundWaveActivity.class);
            }
        });
        findViewById(R.id.bt_suspend_window).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(SuspendWindowActivity.class);
            }
        });
        findViewById(R.id.bt_keyboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(KeyboardActivity.class);
            }
        });
        findViewById(R.id.bt_scan_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(ScanViewActivity.class);
            }
        });

    }

    public void skip(Class<?> cls){
        startActivity(new Intent(this, cls));

    }
}
