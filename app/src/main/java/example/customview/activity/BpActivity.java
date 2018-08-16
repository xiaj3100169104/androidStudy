package example.customview.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.style.framework.R;
import com.style.framework.databinding.ActivityBpBinding;
import com.style.framework.databinding.ActivityTempBinding;
import com.style.view.BloodPressureChart;
import com.style.view.TemperatureLineNew;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BpActivity extends AppCompatActivity {

    ActivityBpBinding bd;
    float max;
    float min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_bp);
        bd.btnRefresh.setOnClickListener(v -> refresh());

    }

    public void refresh() {
        bd.bpLine.setData(getData());
    }

    private List<BloodPressureChart.BloodItem> getData() {
        ArrayList<BloodPressureChart.BloodItem> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            BloodPressureChart.BloodItem b = new BloodPressureChart.BloodItem(random.nextInt(30) + 50, random.nextInt(30) + 90, String.valueOf(i));
            list.add(b);
        }
        return list;
    }
}
