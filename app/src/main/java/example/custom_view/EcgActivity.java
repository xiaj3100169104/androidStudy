package example.custom_view;

import android.text.TextUtils;

import com.style.app.ConfigUtil;
import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class EcgActivity extends BaseTitleBarActivity {

    List<Integer> strings = new ArrayList<>();
    int max = 0, min = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ecg;
    }

    @Override
    protected void initData() {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(ConfigUtil.DIR_APP + "/" + "ecg.txt");
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
