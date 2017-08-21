package example.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.style.base.BaseActivity;
import com.style.framework.R;

import butterknife.OnClick;
import example.bean.TestBean;
import example.db.RealmDBManager;

public class TestRealmActivity extends BaseActivity {

    RealmDBManager manager;

    @Override
    public void initData() {
        manager = RealmDBManager.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_test_realm;
        super.onCreate(savedInstanceState);

    }

    @OnClick(R.id.btn_insert_list)
    public void skip418() {
        for (int i = 0; i < 10; i++) {
            TestBean bean = new TestBean();
            bean.setId(i + "");
            bean.setName("name=" + i);
            bean.setPhone("phone=" + i);
            manager.insertUser(bean);
        }
    }

    @OnClick(R.id.btn_query_all)
    public void skip419() {
        manager.queryAll();
    }

    @OnClick(R.id.btn_clear_table)
    public void skip42() {
        manager.clearTable();
    }

    @OnClick(R.id.btn_insert_new_db)
    public void skip43() {
        for (int i = 0; i < 10; i++) {
            TestBean bean = new TestBean();
            bean.setName("name=" + i);
            bean.setPhone("phone=" + i);
            bean.setAge(i);
            // manager.insertUserDBUp(bean);
        }
    }
}
