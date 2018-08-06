package example.customview.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.style.framework.R;
import com.style.framework.databinding.ActivityCurveBinding;
import com.style.framework.databinding.ActivityHeartLineBinding;
import com.style.view.HeartRateLine;
import com.style.view.HeartRateLineView;
import com.style.view.SleepWeekHistogram;
import com.style.view.TemperatureLineNew;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HeartLineActivity extends AppCompatActivity {

    ActivityHeartLineBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_heart_line);
        bd.btnRefresh.setOnClickListener(v -> refresh());
    }

    public void refresh() {
        bd.heartLine.setData(getHeartData());
        bd.heartLineNew.setData(getHeartRateData());

    }

    private ArrayList<HeartRateLineView.PointItem> getHeartRateData() {
        ArrayList<HeartRateLineView.PointItem> list = new ArrayList<>();
        Random random = new Random();
        HeartRateLineView.PointItem item;
        for (int i = 0; i < 99; i++) {
            String xLabel = "00:" + String.valueOf(i);
            int y;
            if (i > 2 && i < 5)
                y = 0;
            else if (i > 9 && i < 96 && i % 10 == 0)
                y = 0;
            else if (i > 150 && i < 200)
                y = 0;
            else if (i > 200 && i < 250 && i % 2 == 0)
                y = 0;
            else if (i == 998)
                y = 0;
            else
                y = random.nextInt(60) + 50;
            item = new HeartRateLineView.PointItem(xLabel, y);
            list.add(item);
        }
        return list;
    }
    private ArrayList<HeartRateLine.PointItem> getHeartData() {
        ArrayList<HeartRateLine.PointItem> list = new ArrayList<>();
        Random random = new Random();
        HeartRateLine.PointItem item;
        for (int i = 0; i < 99; i++) {
            String xLabel = "00:" + String.valueOf(i);
            int y;
            if (i > 2 && i < 5)
                y = 0;
            else if (i > 9 && i < 96 && i % 10 == 0)
                y = 0;
            else if (i > 150 && i < 200)
                y = 0;
            else if (i > 200 && i < 250 && i % 2 == 0)
                y = 0;
            else if (i == 998)
                y = 0;
            else
                y = random.nextInt(60) + 50;
            item = new HeartRateLine.PointItem(xLabel, y);
            list.add(item);
        }
        return list;
    }
}
