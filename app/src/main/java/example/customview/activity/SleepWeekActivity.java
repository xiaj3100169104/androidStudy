package example.customview.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.style.framework.R;
import com.style.framework.databinding.ActivityCurveBinding;
import com.style.view.HeartRateLine;
import com.style.view.SleepWeekHistogram;
import com.style.view.TemperatureLineNew;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SleepWeekActivity extends AppCompatActivity {

    ActivityCurveBinding bd;
    private String[] mWeeks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_curve);
        bd.btnRefresh.setOnClickListener(v -> refresh());

        mWeeks = getResources().getStringArray(R.array.week_array);
        bd.sleepHistogram.setOnSelectionChangeListener(new SleepWeekHistogram.OnSelectionChangeListener() {
            @Override
            public void onSelectionChanged(int selected) {
                Log.e("CurveActivity2", selected + "");
            }
        });
        bd.sleepHistogram.setData(null, false);
    }

    public void refresh() {
        bd.sleepHistogram.setData(getHistogramData(), true);
    }

    public List<SleepWeekHistogram.PointItem> getHistogramData() {
        List<SleepWeekHistogram.PointItem> mValueList = new ArrayList<>();
        Random random = new Random();
        int y;
        SleepWeekHistogram.PointItem item;
        for (int i = 0; i < 7; i++) {
            y = random.nextInt(180) + 360;
            item = new SleepWeekHistogram.PointItem(mWeeks[i], y);
            mValueList.add(item);
        }
        return mValueList;
    }
}
