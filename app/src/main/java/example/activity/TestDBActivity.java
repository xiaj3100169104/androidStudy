package example.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityTestDbBinding;

import example.bean.TestBean;
import example.db.TestDBManager;

public class TestDBActivity extends BaseActivity {

    ActivityTestDbBinding bd;
    @Override
    public void initData() {

        TestDBManager.getInstance().initialize(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_test_db);
        super.setContentView(bd.getRoot());
    }

    public void skip418(View v) {
        for (int i = 0; i < 10; i++) {
            TestBean bean = new TestBean();
            bean.setName("name=" + i);
            bean.setPhone("phone=" + i);
            TestDBManager.getInstance().insertUser(bean);
        }
    }

    public void skip419(View v) {
        TestDBManager.getInstance().queryAll();
    }

    public void skip42(View v) {
        TestDBManager.getInstance().clearTable();
    }

    public void skip43(View v) {
        for (int i = 0; i < 10; i++) {
            TestBean bean = new TestBean();
            bean.setName("name=" + i);
            bean.setPhone("phone=" + i);
            bean.setAge(i);
            TestDBManager.getInstance().insertUserDBUp(bean);
        }
    }
}
