package example.customView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.databinding.ActivityReportTrendBinding;

import java.util.ArrayList;
import java.util.Random;

public class ReportTrendActivity extends BaseTitleBarActivity {
    ArrayList<Integer> dataList = new ArrayList<>();
    private ActivityReportTrendBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        bd = ActivityReportTrendBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
    }

    private void getData() {
        Random random = new Random(1);
        for (int i = 0; i < 2000; i++) {
            int k = random.nextInt(160) - 80;
            dataList.add(k);
        }
    }

}
