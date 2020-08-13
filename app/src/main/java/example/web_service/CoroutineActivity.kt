package example.web_service

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.TypeReference
import com.style.base.BaseTitleBarActivity
import com.style.entity.UserInfo
import com.style.framework.R
import com.style.framework.databinding.ActivityCoroutineBinding
import kotlinx.coroutines.*

import java.util.ArrayList
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

class CoroutineActivity : BaseTitleBarActivity() {

    lateinit var bd: ActivityCoroutineBinding
    lateinit var mViewModel: WebServiceViewModel

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.activity_coroutine)
        bd = getBinding()
        mViewModel = ViewModelProviders.of(this).get(WebServiceViewModel::class.java)
        mViewModel.content.observe(this, Observer<String> { s -> setContent(s!!) })
    }

    fun parseList(v: View) {
        val list = ArrayList<String>()
        list.add("1")
        list.add("2")
        val s = JSON.toJSONString(list)
        val l2 = JSONArray.parseArray(s, String::class.java) as ArrayList<String>
        //protected TypeReference(Type... actualTypeArguments)不知道为什么能访问
        val type = object : TypeReference<ArrayList<String>>() {

        }
        val l3 = JSON.parseObject(s, type)

        //KuaiDi di = new KuaiDi("外部不能访问protected构造函数");
        var u: UserInfo? = UserInfo("男")
        val u2 = u
        u = null
        val k = u2!!.sex
        val k0 = u2.toString()
        //Java中的hashCode方法就是根据一定的规则将与对象相关的信息（比如对象的存储地址，对象的字段等）映射成一个数值，这个数值称作为散列值。
        val k2 = u2.hashCode()
    }

    val group = Job()

    @ExperimentalCoroutinesApi
    fun onClickCoroutine(v: View) {
        val task = Job()
        val cs = CustomScope(group + Dispatchers.Main)
        val subjob = cs.launch(context = Dispatchers.Main) {
            val data = withContext(Dispatchers.IO) {
                //标记为挂起的网络请求
            }
            //ui更新
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        group.cancel()
    }

    class CustomScope(context: CoroutineContext) : CoroutineScope {
        override val coroutineContext: CoroutineContext = context
    }

    fun searchMobile(v: View) {
        val phone = bd.etPhone.text.toString()
        mViewModel.getPhoneInfo(phone)
    }

    fun searchWeather(v: View) {
        val code = bd.etCityCode.text.toString()
        mViewModel.searchWeather(code)
    }

    fun responseString(v: View) {
        val code = bd.etCityCode.text.toString()
        mViewModel.getWeather(code)
    }

    fun responseGeneralData(v: View) {
        mViewModel.getKuaiDi("", "")
    }

    fun responseGeneralNoData(v: View) {
        mViewModel.login("17364814713", "xj19910809")

    }

    fun setContent(s: String) {
        logE(TAG, s)
        bd.tvResult.text = s
    }

    fun setPhone(s: String) {

    }

    fun setWeather(s: String) {
        logE(TAG, s)
        bd.tvResult.text = s
    }
}
