package example.fragmentAdapter;

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.style.base.BaseRecyclerViewAdapter
import com.style.base.BaseTitleBarActivity
import com.style.framework.databinding.Viewpager2WithViewActivityBinding
import com.style.framework.databinding.Viewpager2WithViewAdapterBinding

class ViewPager2Activity : BaseTitleBarActivity() {

    private lateinit var bd: Viewpager2WithViewActivityBinding
    private lateinit var mViewModel: ViewPager2ActivityViewModel
    private lateinit var fAdapter: StateAdapter
    private val mData = ArrayList<String>()

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        bd = Viewpager2WithViewActivityBinding.inflate(layoutInflater)
        setContentView(bd.root)
        setFullScreenStableDarkMode(true)
        setTitleBarTitle("ViewPager2WithView")

        mViewModel = ViewModelProvider(this).get(ViewPager2ActivityViewModel::class.java)
        mData.addAll(mViewModel.getData())
        fAdapter = StateAdapter(this, mData)
        bd.viewPager2.adapter = fAdapter
    }

    class StateAdapter : BaseRecyclerViewAdapter<String> {

        constructor(context: Context?, dataList: ArrayList<String>) : super(context, dataList)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val bd = Viewpager2WithViewAdapterBinding.inflate(layoutInflater, parent, false)
            return MyViewHolder(bd)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val h = holder as MyViewHolder
            val f = list[position]
            h.bd.tvIndex.text = f
            super.setOnItemClickListener(holder.itemView, position)
        }

        class MyViewHolder internal constructor(bd: Viewpager2WithViewAdapterBinding) : RecyclerView.ViewHolder(bd.getRoot()) {
            val bd: Viewpager2WithViewAdapterBinding

            init {
                this.bd = bd
            }
        }
    }
}
