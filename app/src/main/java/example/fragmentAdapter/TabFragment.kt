package example.fragmentAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BasePagerLazyRefreshFragment
import com.style.framework.databinding.TabSimpleFragmentBinding

class TabFragment : BasePagerLazyRefreshFragment() {

    private lateinit var bd: TabSimpleFragmentBinding
    private var index = -1

    companion object {
        fun newInstance(index: Int): TabFragment {
            val f = TabFragment()
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
        bd = TabSimpleFragmentBinding.inflate(layoutInflater, container, false)
        return bd.root
    }

    override fun onViewIsFirstVisible() {
        initData()
    }

    private fun initData() {
        index++
        bd.tvIndex.text = index.toString()
        bd.btnChange.setOnClickListener {
            index++
            bd.tvIndex.text = index.toString()
        }
    }

}