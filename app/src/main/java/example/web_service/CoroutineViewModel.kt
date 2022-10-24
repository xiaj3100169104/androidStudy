package example.web_service

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.style.base.BaseViewModel
import com.style.data.http.function.impl.WebNetSourceImpl
import com.style.utils.PinyinUtils
import example.address.ContactHelper
import example.address.UploadPhoneComparator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CoroutineViewModel(application: Application) : BaseViewModel(application) {

    var content = MutableLiveData<String>()

    fun getContact() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val list = ContactHelper.getContacts(getApplication())
                    if (null != list) {
                        val size = list.size
                        for (i in 0 until size) {
                            val sortLetter =
                                PinyinUtils.getAbbreviation(list[i].name).substring(0, 1)
                            list[i].sortLetters = sortLetter
                        }
                        // 根据a-z进行排序源数据
                        Collections.sort(list, UploadPhoneComparator())
                        list.forEach {
                            logI("info", it.toString())
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    fun getWeather(code: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    var str = WebNetSourceImpl.mAPI2.getWeatherInfo2(code, "")
                    str?.let {
                        logI("str", str)
                        //showToast("查询天气成功")
                        content.postValue(str)
                        var kd = WebNetSourceImpl.mAPI2.getKuaiDi2("yuantong", "11111111111")
                        kd?.let { it1 -> logI("kd", it1) }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }
}