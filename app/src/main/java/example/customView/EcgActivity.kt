package example.customView;

import android.os.Bundle;

import com.style.app.FileDirConfig;
import com.style.base.activity.BaseTitleBarActivity;
import com.style.framework.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException

class EcgActivity : BaseTitleBarActivity() {

    private val strings = arrayListOf<Int>()
    var max = 0
    var min = 0

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.activity_ecg)
        initData()
    }

    private fun initData() {
        val fileReader: FileReader?
        try {
            fileReader = FileReader(FileDirConfig.DIR_APP + "/" + "ecg.txt")
            val bufferedReader = BufferedReader(fileReader)
            var line: String?
            var v: Int
            while (true) {
                line = bufferedReader.readLine() ?: break
                println(line)
                if (!line.isNullOrEmpty()) {
                    v = line.toFloat().toInt()
                    max = if (v > max) v else max
                    min = if (v < min) v else min
                    strings.add(v)
                }
            }
            bufferedReader.close() //关闭缓冲读取器
            println(strings.size)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
