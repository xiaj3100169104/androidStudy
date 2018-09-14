package example.webservice;

import android.view.View;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityWebserviceBinding;

public class WebServiceActivity extends BaseTitleBarActivity {

    ActivityWebserviceBinding bd;
    WebServicePresenter presenter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_webservice;
    }

    @Override
    public void initData() {
        bd = getBinding();
        presenter = getViewModel(WebServicePresenter.class);
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
        presenter.getWeather(code);
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
}
