package example.viewPagerTabLayout

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.util.Log
import android.view.View
import com.style.base.BaseFragment

import com.style.framework.R
import com.style.framework.databinding.FragmentBottomSheetDialogBinding


class BottomSheetDialogFragment : BaseFragment() {

    private lateinit var bd: FragmentBottomSheetDialogBinding
    private var param: String? = "null"

    companion object {

        fun newInstance(i: String): BottomSheetDialogFragment {
            val instance = BottomSheetDialogFragment()
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

    override fun getLayoutResId(): Int {
        return R.layout.fragment_bottom_sheet_dialog
    }

    override fun initData() {
        bd = getBinding()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e(param, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        bd.tvContent.text = param
        bd.tvContent.setOnClickListener {
            var dialog = BottomSheetDialog(context!!)
            dialog.setContentView(layoutInflater.inflate(R.layout.fragment_home_3, null))
            dialog.show()
        }
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
