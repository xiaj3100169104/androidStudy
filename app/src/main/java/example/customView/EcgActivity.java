package example.customView;

import android.os.Bundle;
import android.text.TextUtils;

import com.style.app.FileDirConfig;
import com.style.base.BaseDefaultTitleBarActivity;
import com.style.framework.R;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class EcgActivity extends BaseDefaultTitleBarActivity {

    List<Integer> strings = new ArrayList<>();
    int max = 0, min = 0;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_ecg);
        initData();
    }

    protected void initData() {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(FileDirConfig.DIR_APP + "/" + "ecg.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String str = null;
            int v;
            while ((str = bufferedReader.readLine()) != null) {
                System.out.println(str);
                if (!TextUtils.isEmpty(str)) {
                    v = Double.valueOf(str).intValue();
                    max = v > max ? v : max;
                    min = v < min ? v : min;
                    strings.add(v);
                }
            }

            System.out.println(strings.size());

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }
}
