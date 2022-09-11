package example.scroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.style.framework.databinding.ActivityScrollingParallaxBinding

class ScrollingParallaxActivity : AppCompatActivity() {

    private lateinit var bd: ActivityScrollingParallaxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bd = ActivityScrollingParallaxBinding.inflate(layoutInflater)
        setContentView(bd.root)
        bd.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

    }
}
