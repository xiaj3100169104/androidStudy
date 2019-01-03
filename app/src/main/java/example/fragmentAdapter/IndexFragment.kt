package example.fragmentAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BasePagerLazyRefreshFragment
import com.style.framework.R
import kotlinx.android.synthetic.main.fragment_adapter_fragment.*

class IndexFragment : BasePagerLazyRefreshFragment() {

    private var index = -1

    companion object {
        fun newInstance(index: Int): IndexFragment {
            val f = IndexFragment()
            val b = Bundle()
            b.putInt("index", index)
            f.arguments = b
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        index = arguments?.getInt("index", -1)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_adapter_fragment, container, false)
    }

    override fun onFirstViewVisible() {
        initData()
    }

    private fun initData() {
        index++
        tv_index.text = index.toString()
        btn_change.setOnClickListener {
            index++
            tv_index.text = index.toString()
        }
    }

}