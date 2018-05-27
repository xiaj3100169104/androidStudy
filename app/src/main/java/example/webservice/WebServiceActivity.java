package example.webservice;

import android.view.View;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.framework.R;
import com.style.framework.databinding.ActivityWebserviceBinding;
import com.style.net.bean.UserInfo;
import com.style.net.core2.RetrofitImpl;
import com.style.net.core2.callback.CustomHttpThrowable;

import io.reactivex.functions.Consumer;

public class WebServiceActivity extends BaseActivity {

    ActivityWebserviceBinding bd;
    WebServicePresenter presenter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_webservice;
    }

    @Override
    protected BaseActivityPresenter getPresenter() {
        presenter = new WebServicePresenter(this);
        return presenter;
    }

    @Override
    public void initData() {
        bd = getBinding();
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

    public void searchWeatherSuccess(String s) {
        logE(TAG, s);
        bd.tvResult.setText(s);
    }

    public void setPhoneInfo(String s) {
        logE(TAG, s);
        bd.tvResult.setText(s);
    }

    public void setWeatherInfo(String s) {
        logE(TAG, s);
        bd.tvResult.setText(s);
    }

    public void setKuaiDiInfo(String s) {
        logE(TAG, s.toString());
        bd.tvResult.setText(s.toString());
    }
}
