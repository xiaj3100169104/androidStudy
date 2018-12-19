package example.editLayout

import android.app.Application
import com.style.base.BaseViewModel

class StateFragmentActivityViewModel : BaseViewModel {
    val titles: ArrayList<String> = arrayListOf()
    var datas = hashMapOf<String, Int>()

    constructor(application: Application) : super(application)

    fun getTitleData(): ArrayList<String> {
        for (i in 0 until 10) {
            val title = i.toString()
            titles.add(title)
            datas[title] = i
        }
        return titles
    }



}