package example.customview.activity;

import android.content.Intent;

import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.CustomViewMainBinding;

public class CustomViewMainActivity extends BaseActivity {

    private CustomViewMainBinding bd;

    @Override
    protected boolean isGeneralTitleBar() {
        return false;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.custom_view_main;
    }

    @Override
    protected void initData() {
        bd = getBinding();

        bd.btNotifyPoint.setOnClickListener(v -> skip(CustomNotifyViewActivity.class));
        bd.btProgress1.setOnClickListener(v -> skip(HorizontalProgressActivity.class));
        bd.btProgress2.setOnClickListener(v -> skip(CircleProgressBarActivity.class));
        bd.btWriteWord.setOnClickListener(v -> skip(WriteWordActivity.class));
        bd.btWaterPolo.setOnClickListener(v -> skip(WaterPoloActivity.class));
        bd.btSoundWave.setOnClickListener(v -> skip(SoundWaveActivity.class));
        bd.btSuspendWindow.setOnClickListener(v -> skip(SuspendWindowActivity.class));
        bd.btKeyboard.setOnClickListener(v -> skip(KeyboardActivity.class));
        bd.btScanView.setOnClickListener(v -> skip(ScanViewActivity.class));
        bd.btCurve.setOnClickListener(v -> skip(SleepWeekActivity.class));
        bd.btTemp.setOnClickListener(v -> skip(TempActivity.class));
        bd.btHeart.setOnClickListener(v -> skip(HeartLineActivity.class));
        bd.btBp.setOnClickListener(v -> skip(BpActivity.class));

    }

    public void skip(Class<?> cls) {
        startActivity(new Intent(this, cls));

    }
}
