package test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.style.framework.R;
import com.style.view.progressbar.ArcProgress;
import com.style.view.progressbar.CirclePercentView;
import com.style.view.progressbar.CircleProgress;


public class CircleProgressBarActivity extends ActionBarActivity {
    private CirclePercentView mCirclePercentView;
    private CircleProgress circleProgress;
    private ArcProgress arcProgress;
    private Button mButton;
    private CirclePercentView mCirclePercentView2;
    private CircleProgress circleProgress2;
    private ArcProgress arcProgress2;
    private CirclePercentView mCirclePercentView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_progress);
        mCirclePercentView = (CirclePercentView) findViewById(R.id.circleView);
        mCirclePercentView2 = (CirclePercentView) findViewById(R.id.circleView2);
        mCirclePercentView3 = (CirclePercentView) findViewById(R.id.circleView3);

        circleProgress = (CircleProgress) findViewById(R.id.circle_progress);
        arcProgress = (ArcProgress) findViewById(R.id.arc_progress);
        circleProgress2 = (CircleProgress) findViewById(R.id.circle_progress2);
        arcProgress2 = (ArcProgress) findViewById(R.id.arc_progress2);
        mButton = (Button) findViewById(R.id.button);
        mCirclePercentView2.setCurPercent(0);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = (int)(Math.random()*100);

                mCirclePercentView.setCurPercent(n);
                arcProgress.setProgress(n);
                circleProgress.setProgress(n);

                mCirclePercentView2.setPercentWithAnimation(n);
                arcProgress2.setPercentWithAnimation(n);
                circleProgress2.setPercentWithAnimation(n);
            }
        });

        //mCirclePercentView3.startAnimation();
    }

}
