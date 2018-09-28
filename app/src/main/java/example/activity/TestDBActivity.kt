package example.activity;

import android.view.View;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityTestDbBinding;

import example.bean.TestBean;
import example.db.TestDBManager;

public class TestDBActivity : BaseTitleBarActivity() {

    lateinit var bd:ActivityTestDbBinding ;
    override fun getLayoutResId():Int {
        return R.layout.activity_test_db;
    }

    override fun initData() {
        bd = getBinding();
        TestDBManager.getInstance().initialize(this);
    }

    public fun skip418(v: View) {
        for (i in 0..10) {
            var bean = TestBean();
            bean.setName("name=" + i);
            bean.setPhone("phone=" + i);
            TestDBManager.getInstance().insertUser(bean);
        }
    }

    public fun skip419(v: View) {
        TestDBManager.getInstance().queryAll();
    }

    public fun skip42(v: View) {
        TestDBManager.getInstance().clearTable();
    }

    public fun skip43(v: View) {

        for (i in 0 until 10) {
            var bean = TestBean();
            bean.setName("name=" + i);
            bean.setPhone("phone=" + i);
            bean.setAge(i);
            TestDBManager.getInstance().insertUserDBUp(bean);
        }
    }
}
