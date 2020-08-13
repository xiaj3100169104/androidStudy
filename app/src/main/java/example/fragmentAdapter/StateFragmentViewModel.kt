package example.fragmentAdapter

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.style.base.BaseViewModel

class StateFragmentViewModel : BaseViewModel {

    var data = MutableLiveData<ArrayList<String>>()

    constructor(application: Application) : super(application)

    fun getListData(title: String) {
        var datas = arrayListOf<String>()
        for (i in 0 until 10) {
            val title = i.toString()
            datas.add(title)
        }
        data.postValue(datas)
    }
}