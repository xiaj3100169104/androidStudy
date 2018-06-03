package fussen.cc.barchart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.style.framework.R;

public class CustomScrollMainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_scrll_main);

        Button more = (Button) findViewById(R.id.more);
        Button small = (Button) findViewById(R.id.small);



        more.setOnClickListener(this);
        small.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more:
                startActivity(new Intent(this,ScrollBarActivity.class));
                break;
            case R.id.small:
                startActivity(new Intent(this,NormalBarActivity.class));
                break;
        }
    }
}
