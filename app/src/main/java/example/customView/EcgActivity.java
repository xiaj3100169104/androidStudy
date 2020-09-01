package example.customView;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityEcgBinding;
import com.style.view.healthy.EcgView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class EcgActivity extends BaseTitleBarActivity {
    ArrayList<Integer> dataList = new ArrayList<>();
    private ActivityEcgBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_ecg);
        bd = getBinding();
        bd.view.setListener(new EcgView.PlayListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onEnd() {

            }
        });
        //getData(this, "ecg.txt");//插值 rate改为0
        getData();
        bd.view.setData(dataList);
    }

    public void startPlay(View v) {
        bd.view.start();
    }

    public void pause(View v) {
        bd.view.pause();
    }

    private void getData() {
        Random random = new Random(1);
        for (int i = 0; i < 3000; i++) {
            int k = random.nextInt(160) - 80;
            dataList.add(k);
        }
    }

    private void getData(Context context, String fileName) {
        dataList.clear();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                if (!TextUtils.isEmpty(line)) {
                    int k = Integer.parseInt(line) - 2048;
                    if (k > 80)
                        k = 80;
                    if (k < -80)
                        k = -80;
                    dataList.add(k);
                }
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
