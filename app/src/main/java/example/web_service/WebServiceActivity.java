package example.web_service;

import android.os.Bundle;
import android.view.View;

import com.style.base.activity.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityWebserviceBinding;

import org.jetbrains.annotations.Nullable;

public class WebServiceActivity extends BaseDefaultTitleBarActivity implements WebServiceView {

    ActivityWebserviceBinding bd;
    WebServiceViewModel presenter;
    WebServicePresenter mPresenter;
    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_webservice);
        bd = getBinding();
        presenter = getViewModel(WebServiceViewModel.class);
        mPresenter = new WebServicePresenter(this);
        presenter.content.observe(this, s -> setContent(s));
    }

    public void searchMobile(View v) {
        final String phone = bd.etPhone.getText().toString();
        presenter.getPhoneInfo(phone);
    }

    public void searchWeather(View v) {
        final String code = bd.etCityCode.getText().toString();
        presenter.searchWeather(code);

    }

    public void responseString(View v) {
        final String code = bd.etCityCode.getText().toString();
        mPresenter.getWeather(code);
    }

    public void responseGeneralData(View v) {
        presenter.getKuaiDi("", "");
    }

    public void responseGeneralNoData(View v) {
        presenter.login("17364814713", "xj19910809");

    }

    public void setContent(String s) {
        logE(getTAG(), s.toString());
        bd.tvResult.setText(s.toString());
    }

    @Override
    public void setPhone(String s) {

    }

    @Override
    public void setWeather(String s) {
        logE(getTAG(), s.toString());
        bd.tvResult.setText(s.toString());
    }
}
