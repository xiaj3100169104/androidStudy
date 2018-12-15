package example.viewPagerTabLayout

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseFragment

import com.style.framework.R
import com.style.framework.databinding.FragmentTablayoutBinding


class TabLayoutFragment : BaseFragment() {

    private lateinit var bd: FragmentTablayoutBinding
    private var param: String? = "null"

    companion object {

        fun newInstance(i: String): TabLayoutFragment {
            val instance = TabLayoutFragment()
            val b = Bundle()
            b.putString("num", i)
            instance.arguments = b
            return instance
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e(param, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        param = arguments!!.getString("num")
        Log.e(param, "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tablayout, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e(param, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        bd = getBinding(view)
        bd.tvContent.text = param
        val behavior = BottomSheetBehavior.from(bd.scroll)
        behavior.peekHeight = 50
        bd.tvContent.setOnClickListener {
            if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            } else {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.e(TAG, "onStateChanged  $newState")

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.e(TAG, "onActivityCreated  $slideOffset")

            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(param, "onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.e(param, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e(param, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e(param, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e(param, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(param, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(param, "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e(param, "onDetach")
    }

}
