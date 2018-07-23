package example.activity;

import android.view.View;

import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityGreenDaoBinding;

import example.bean.TestGreenBean;
import example.greendao.dao.GreenDaoManager;

public class GreenDaoActivity extends BaseActivity {

    ActivityGreenDaoBinding bd;
    GreenDaoManager manager;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_green_dao;
    }

    @Override
    public void initData() {
        bd = getBinding();

        manager = GreenDaoManager.getInstance();
    }

    public void skip418(View v) {
        for (int i = 0; i < 10; i++) {
            TestGreenBean bean = new TestGreenBean();
            bean.setId(i + "");
            bean.setName("name=" + i);
            bean.setPhone("phone=" + i);
            bean.setExtra("extra=" + i);
            manager.insertUser(bean);
        }
    }

    public void skip42(View v) {
        manager.clearGreenTable();
    }

    public void skip44(View v) {
        TestGreenBean bean = new TestGreenBean();
        bean.setId("9");
        //bean.setName("name=" + i);
        //bean.setPhone("phone=" + i);
        bean.setAge(9);
        manager.update(bean);
    }

    public void skip419(View v) {
        manager.queryAll();
    }


    public void skip420(View v) {
        manager.queryAsc();
    }

    public void skip421(View v) {
        manager.queryDesc();
    }

    public void skip422(View v) {
        manager.queryWhereAnd();
    }

    public void skip423(View v) {
        manager.queryWhereOr();
    }

    public void skip424(View v) {
        manager.queryWhereBetween();
    }

    public void skip43(View v) {
        for (int i = 0; i < 10; i++) {
            TestGreenBean bean = new TestGreenBean();
            bean.setName("name" + i);
            bean.setPhone("phone" + i);
            bean.setAge(i);
            // manager.insertUserDBUp(bean);
        }
    }
}
