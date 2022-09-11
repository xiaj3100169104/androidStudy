package example.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.ndk.JniCommon;
import com.ndk.JniTest;
import com.style.framework.R;
import com.style.framework.databinding.ActivityJniTestBinding;

public class JniTestActivity extends AppCompatActivity {

    private ActivityJniTestBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityJniTestBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());

        String tt = JniCommon.stringFromJNI();
        bd.sampleText.setText(tt);
        String kk = tt;
        String obj = "obj";
        short s = 1;
        long l = 20;
        byte b = 127;
        JniTest.testShort(s);

        JniTest.testBasicDataType(s, 1, l, 1.0f, 10.5, 'A', false, b);
        JniTest.testJString("中国", obj, new MyClass(), new int[] {});
    }

    public static class MyClass {
    }
}
