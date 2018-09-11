package example.customview.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.style.framework.R;
import com.style.framework.databinding.ActivityCircleProgressBinding;
import com.style.view.progressbar.CircleArcProgressBar;
import com.style.view.progressbar.CircleProgress;


public class CircleProgressBarActivity extends AppCompatActivity {

    private ActivityCircleProgressBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_circle_progress);

        bd.button.setOnClickListener(v -> {
            int n = (int) (Math.random() * 100);
            bd.progressBar.setProgress(n);
            bd.circleProgress.setProgress(n);
            bd.arcProgress.setProgress(n);
        });
        bd.button2.setOnClickListener(v -> {
            int n = (int) (Math.random() * 100);
            bd.progressBar.setPercentWithAnimation(n);
            bd.circleProgress.setPercentWithAnimation(n);
            bd.arcProgress.setPercentWithAnimation(n);
        });

    }

}
