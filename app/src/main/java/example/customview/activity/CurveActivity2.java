package example.customview.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.style.framework.R;
import com.style.framework.databinding.ActivityCurveBinding;
import com.style.view.HeartRateLine;

import java.util.ArrayList;
import java.util.Random;

public class CurveActivity2 extends AppCompatActivity {

    ActivityCurveBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_curve);
        /*bd.heartLine.postDelayed(new Runnable() {
            @Override
            public void run() {
                bd.heartLine.scrollTo(900, 0);
            }
        }, 2000);*/
    }

    public void refresh(View view) {
        bd.heartLine.setData(getTestData());
    }

    private ArrayList<HeartRateLine.PointItem> getTestData() {
        ArrayList<HeartRateLine.PointItem> list = new ArrayList<>();
        Random random = new Random();
        HeartRateLine.PointItem item;
        for (int i = 0; i < 1000; i++) {
            String xLabel = "00:00";// + String.valueOf(i);
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
