package example.fragmentAdapter

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseFragment
import com.style.framework.R
import kotlinx.android.synthetic.main.fragment_adapter_fragment.*

class StateFragment : BaseFragment() {

    private lateinit var title: String
    private lateinit var mViewModel: StateFragmentViewModel

    companion object {
        fun newInstance(title: String?): StateFragment {
            val f = StateFragment()
            val b = Bundle()
            if (title != null) {
                b.putString("title", title)
            }
            f.arguments = b
            return f
        }
    }

    private var dataList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString("title")!!
        mViewModel = getViewModel(StateFragmentViewModel::class.java)
        mViewModel.data.observe(this, Observer<ArrayList<String>> { d ->
            if (d != null) {
                this.dataList.addAll(d)
                refreshView()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_adapter_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            var list = savedInstanceState.getSerializable("list") as ArrayList<String>
            this.dataList.addAll(list)
            refreshView()
        }
        refreshData()
        btn_change.setOnClickListener {
            refreshData()
        }
    }

    private fun refreshData() {
        mViewModel.getListData(title)
    }

    private fun refreshView() {
        dataList.forEachIndexed { index, s ->
            logE(TAG, s)
        }
    }

    /**
     * fragment被销毁用该方法保存数据
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList("list", dataList)
        super.onSaveInstanceState(outState)
    }

    /**
     * 可以在宿主model中获取数据
     */
    private lateinit var mActivityViewModel: StateFragmentActivityViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mActivityViewModel = ViewModelProviders.of(context as FragmentActivity).get(StateFragmentActivityViewModel::class.java)
    }

}