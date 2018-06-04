package example.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.style.framework.R;
import com.style.view.WaterPoloProgress;

public class WaterPoloActivity extends AppCompatActivity {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_polo);
        final WaterPoloProgress sineCurve = (WaterPoloProgress) findViewById(R.id.custom_view);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = (int)(Math.random()*100);

                sineCurve.setPercentWithAnimation(n);

            }
        });
    }
}
