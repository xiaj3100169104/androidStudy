package example.customView;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityEcgBinding;
import com.style.framework.databinding.ActivityReportTrendBinding;
import com.style.view.healthy.EcgView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class ReportTrendActivity extends BaseTitleBarActivity {
    ArrayList<Integer> dataList = new ArrayList<>();
    private ActivityReportTrendBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_report_trend);
        bd = getBinding();
        //bd.viewt.setData(dataList);
    }

    private void getData() {
        Random random = new Random(1);
        for (int i = 0; i < 2000; i++) {
            int k = random.nextInt(160) - 80;
            dataList.add(k);
        }
    }

}
