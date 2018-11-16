package example.db;

import android.os.Bundle
import com.style.base.BaseTitleBarActivity
import com.style.framework.R
import kotlinx.android.synthetic.main.activity_test_room.*

class TestRoomActivity : BaseTitleBarActivity() {
    private lateinit var mViewModel: TestRoomViewModel

    override fun getLayoutResId(): Int {
        return R.layout.activity_test_room
    }

    override fun initData() {
        setToolbarTitle("room test")
        mViewModel = getViewModel(TestRoomViewModel::class.java)
        btn_room_insert_one.setOnClickListener { mViewModel.saveOne() }
        btn_room_insert_list.setOnClickListener { mViewModel.saveList() }
        btn_room_query_all.setOnClickListener { mViewModel.queryAll() }
        btn_room_clear_table.setOnClickListener { mViewModel.deleteAll() }
        btn_room_query_by_id.setOnClickListener { mViewModel.findById() }
    }


}
