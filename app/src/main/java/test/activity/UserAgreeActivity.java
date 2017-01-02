package test.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;

import java.io.IOException;
import java.io.InputStream;

public class UserAgreeActivity extends BaseToolBarActivity {

    private TextView tv_agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_user_agree;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        setToolbarTitle("用户协议");

        tv_agree = (TextView) findViewById(R.id.tv_useragree);
        readData();
    }

    public void readData() {
        try {
            // Return an AssetManager instance for your application's package
            InputStream is = getAssets().open("useragree.txt");
            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");

            // Finally stick the string into the text view.
            tv_agree.setText(text);
        } catch (IOException e) {
            // Should never happen!
            throw new RuntimeException(e);
        }

    }
}
