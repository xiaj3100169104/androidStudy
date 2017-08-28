package example.activity;

import android.os.Bundle;

import com.style.base.BaseActivity;
import com.style.framework.R;

import butterknife.OnClick;
import example.bean.TestBean;
import example.bean.TestGreenBean;
import example.db.RealmDBManager;
import example.greendao.dao.GreenDaoManager;

public class GreenDaoActivity extends BaseActivity {

    GreenDaoManager manager;

    @Override
    public void initData() {
        manager = GreenDaoManager.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_green_dao;
        super.onCreate(savedInstanceState);

    }

    @OnClick(R.id.btn_insert_list)
    public void skip418() {
        for (int i = 0; i < 10; i++) {
            TestGreenBean bean = new TestGreenBean();
            bean.setId(i + "");
            bean.setName("name=" + i);
            bean.setPhone("phone=" + i);
            bean.setExtra("extra=" + i);
            manager.insertUser(bean);
        }
    }

    @OnClick(R.id.btn_clear_table)
    public void skip42() {
        manager.clearGreenTable();
    }

    @OnClick(R.id.btn_update)
    public void skip44() {
        TestGreenBean bean = new TestGreenBean();
        bean.setId("9");
        //bean.setName("name=" + i);
        //bean.setPhone("phone=" + i);
        bean.setAge(9);
        manager.update(bean);
    }

    @OnClick(R.id.btn_query_all)
    public void skip419() {
        manager.queryAll();
    }


    @OnClick(R.id.btn_query_asc)
    public void skip420() {
        manager.queryAsc();
    }

    @OnClick(R.id.btn_query_desc)
    public void skip421() {
        manager.queryDesc();
    }

    @OnClick(R.id.btn_query_and)
    public void skip422() {
        manager.queryWhereAnd();
    }

    @OnClick(R.id.btn_query_or)
    public void skip423() {
        manager.queryWhereOr();
    }

    @OnClick(R.id.btn_query_between)
    public void skip424() {
        manager.queryWhereBetween();
    }

    @OnClick(R.id.btn_insert_new_db)
    public void skip43() {
        for (int i = 0; i < 10; i++) {
            TestGreenBean bean = new TestGreenBean();
            bean.setName("name" + i);
            bean.setPhone("phone" + i);
            bean.setAge(i);
            // manager.insertUserDBUp(bean);
        }
    }
}
