package example.db;

import android.view.View;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;

import example.bean.TestBean;
import kotlinx.android.synthetic.main.activity_test_db.*

class TestDBActivity : BaseTitleBarActivity() {

    override fun getLayoutResId():Int {
        return R.layout.activity_test_db;
    }

    private lateinit var mViewModel: TestRoomViewModel

    override fun initData() {
        mViewModel = getViewModel(TestRoomViewModel::class.java)
        TestDBManager.getInstance().initialize(this)
        btn_room_insert_one.setOnClickListener { mViewModel.saveOne() }
        btn_room_insert_list.setOnClickListener { mViewModel.saveList() }
        btn_room_query_all.setOnClickListener { mViewModel.queryAll() }
        btn_room_clear_table.setOnClickListener { mViewModel.deleteAll() }
        btn_room_query_by_id.setOnClickListener { mViewModel.findById() }
    }

    public fun skip418(v: View) {
        for (i in 0..10) {
            val bean = TestBean();
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
