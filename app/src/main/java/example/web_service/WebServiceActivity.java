package example.web_service;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.style.base.activity.BaseTitleBarActivity;
import com.style.entity.KuaiDi;
import com.style.entity.UserInfo;
import com.style.framework.R;
import com.style.framework.databinding.ActivityWebserviceBinding;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class WebServiceActivity extends BaseTitleBarActivity {

    ActivityWebserviceBinding bd;
    WebServiceViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_webservice);
        bd = getBinding();
        mViewModel = getViewModel(WebServiceViewModel.class);
        mViewModel.content.observe(this, s -> setContent(s));
    }

    public void parseList(View v) {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        String s = JSON.toJSONString(list);
        ArrayList<String> l2 = (ArrayList<String>) JSONArray.parseArray(s, String.class);
        //protected TypeReference(Type... actualTypeArguments)不知道为什么能访问
        TypeReference<ArrayList<String>> type = new TypeReference<ArrayList<String>>() {
        };
        ArrayList<String> l3 = JSON.parseObject(s, type);

        //KuaiDi di = new KuaiDi("外部不能访问protected构造函数");
        UserInfo u = new UserInfo("男");
        UserInfo u2 = u;
        u = null;
        String k = u2.getSex();
        String k0 = u2.toString();
        //Java中的hashCode方法就是根据一定的规则将与对象相关的信息（比如对象的存储地址，对象的字段等）映射成一个数值，这个数值称作为散列值。
        int k2 = u2.hashCode();
    }

    public void searchMobile(View v) {
        final String phone = bd.etPhone.getText().toString();
        mViewModel.getPhoneInfo(phone);
    }

    public void searchWeather(View v) {
        final String code = bd.etCityCode.getText().toString();
        mViewModel.searchWeather(code);
    }

    public void responseString(View v) {
        final String code = bd.etCityCode.getText().toString();
        mViewModel.getWeather(code);
    }

    public void responseGeneralData(View v) {
        mViewModel.getKuaiDi("", "");
    }

    public void responseGeneralNoData(View v) {
        mViewModel.login("17364814713", "xj19910809");

    }

    public void setContent(String s) {
        logE(getTAG(), s.toString());
        bd.tvResult.setText(s.toString());
    }

    public void setPhone(String s) {

    }

    public void setWeather(String s) {
        logE(getTAG(), s.toString());
        bd.tvResult.setText(s.toString());
    }
}
