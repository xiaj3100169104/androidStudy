package example.web_service;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.databinding.ActivityWebserviceBinding;

import org.jetbrains.annotations.Nullable;

public class WebServiceActivity extends BaseTitleBarActivity {

    ActivityWebserviceBinding bd;
    WebServiceViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        bd = ActivityWebserviceBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setTitleBarTitle("webservice");
        mViewModel = new ViewModelProvider(this).get(WebServiceViewModel.class);
        mViewModel.content.observe(this, s -> setContent(s));
    }

    public void searchMobile(View v) {
        final String phone = bd.etPhone.getText().toString();
        mViewModel.getPhoneInfo(phone);
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

}
