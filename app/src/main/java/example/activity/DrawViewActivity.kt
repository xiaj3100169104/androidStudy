package example.activity

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message

import com.style.framework.R


class DrawViewActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_view)
        val handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                this@DrawViewActivity.runOnUiThread { }
            }
        }

    }
}
