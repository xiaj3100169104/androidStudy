package example.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.utils.AssetsUtil;

import java.io.IOException;

public class SoftMode1Activity extends AppCompatActivity {

    private TextView tv_agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_mode_1);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
