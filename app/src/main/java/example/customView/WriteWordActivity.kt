package example.customView

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import com.style.framework.R

class WriteWordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_word)

        for (i in 1..5 step 2) {
            Log.e("WriteWordActivity", i.toString())
        }
        for (i in 1 until 5 step 2) {
            Log.e("WriteWordActivity", i.toString())

        }
        for (i in 10 downTo 1 step 2) {
            println("i=$i")
        }
    }
}
