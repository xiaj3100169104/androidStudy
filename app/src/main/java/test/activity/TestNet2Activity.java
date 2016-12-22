package test.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
/**
 * Created by xiajun on 2016/10/8.
 */
public class TestNet2Activity extends BaseToolBarActivity {

    private TextView text;
    private Button refresh;
    private Button loadmore;

    int page = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.test_rx_2;
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initData() {
        setToolbarTitle("rxtest");

        text = (TextView) findViewById(R.id.editText);
        refresh = (Button) findViewById(R.id.clear);
        loadmore = (Button) findViewById(R.id.request);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 0;
                getData("0");
                text.setText(null);
            }
        });
        loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData("6");
            }
        });
        getData("1");
        getData("2");
        getData("3");
        getData("4");
        getData("5");
    }

    private void getData(final String param) {

    }
}