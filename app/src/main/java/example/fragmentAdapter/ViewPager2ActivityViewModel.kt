package example.fragmentAdapter

import android.app.Application
import com.style.base.BaseViewModel

class ViewPager2ActivityViewModel : BaseViewModel {

    constructor(application: Application) : super(application)

    fun getData(): ArrayList<String> {
        var datas = arrayListOf<String>()
        for (i in 0 until 10) {
            val s = i.toString()
            datas.add(s)
        }
        return datas
    }



}