package example.db;

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.style.base.BaseTitleBarActivity
import com.style.framework.databinding.ActivityTestRoomBinding

class TestRoomActivity : BaseTitleBarActivity() {

    private lateinit var bd: ActivityTestRoomBinding
    private lateinit var mViewModel: TestRoomViewModel

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        bd = ActivityTestRoomBinding.inflate(layoutInflater)
        setContentView(bd.root)
        setTitleBarTitle("room test")
        mViewModel = ViewModelProvider(this).get(TestRoomViewModel::class.java)
        bd.btnRoomInsertOne.setOnClickListener { mViewModel.saveOne() }
        bd.btnRoomInsertList.setOnClickListener { mViewModel.saveList() }
        bd.btnRoomQueryAll.setOnClickListener { mViewModel.queryAll() }
        bd.btnRoomClearTable.setOnClickListener { mViewModel.deleteAll() }
        bd.btnRoomQueryById.setOnClickListener { mViewModel.findById() }
    }


}
