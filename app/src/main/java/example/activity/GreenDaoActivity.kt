package example.activity;

import android.view.View;
import com.style.app.ConfigUtil

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityGreenDaoBinding;

import example.bean.TestGreenBean;
import example.greendao.dao.GreenDaoManager;

public class GreenDaoActivity : BaseTitleBarActivity() {

    lateinit var bd: ActivityGreenDaoBinding;
    lateinit var manager: GreenDaoManager;

    override fun getLayoutResId(): Int {
        return R.layout.activity_green_dao;
    }

    override fun initData() {
        bd = getBinding();
        manager = GreenDaoManager.getInstance();
    }

    fun skip418(v: View) {
        for (i in 0 until 10) {
            var bean = TestGreenBean();
            bean.setId(i.toString());
            bean.setName("name=" + i);
            bean.setPhone("phone=" + i);
            bean.setExtra("extra=" + i);
            manager.insertUser(bean);
        }
    }

    fun skip42(v: View) {
        manager.clearGreenTable();
    }

    fun skip44(v: View) {
        var bean = TestGreenBean();
        bean.setId("9");
        //bean.setName("name=" + i);
        //bean.setPhone("phone=" + i);
        bean.setAge(9);
        manager.update(bean);
    }

    fun skip419(v: View) {
        manager.queryAll();
    }

    fun skip420(v: View) {
        manager.queryAsc();
    }

    fun skip421(v: View) {
        manager.queryDesc();
    }

    fun skip422(v: View) {
        manager.queryWhereAnd();
    }

    fun skip423(v: View) {
        manager.queryWhereOr();
    }

    fun skip424(v: View) {
        manager.queryWhereBetween();
    }

    fun skip43(v: View) {
        for (i in 0 until 10) {
            var bean = TestGreenBean();
            bean.setName("name" + i);
            bean.setPhone("phone" + i);
            bean.setAge(i);
            // manager.insertUserDBUp(bean);
        }
    }
}
