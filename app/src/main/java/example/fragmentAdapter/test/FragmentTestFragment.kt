package example.fragmentAdapter.test

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseFragment
import com.style.framework.R
import kotlinx.android.synthetic.main.fragment_test_fragment.*

class FragmentTestFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_test_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fake_status_bar.layoutParams.height = getStatusHeight()
        } else {
            fake_status_bar.layoutParams.height = 0
        }
    }
}