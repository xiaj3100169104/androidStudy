package example.fragmentAdapter.test

import android.os.Bundle
import com.style.base.BaseActivity
import com.style.framework.R

class FragmentTestActivity : BaseActivity() {

    private lateinit var cartFragment: FragmentTestFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_test_activity)
        setFullScreenStableDarkMode(true)
        initViewAndEvent()
    }

    private fun initViewAndEvent() {
        cartFragment = FragmentTestFragment()
        supportFragmentManager.beginTransaction().add(R.id.cart_fragment_container, cartFragment, "cartFragment")
                .commitAllowingStateLoss()
    }
}
