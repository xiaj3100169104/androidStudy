package example.webservice;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.framework.R;
import com.style.framework.databinding.ActivityWebserviceBinding;
import com.style.app.AccountManager;
import com.style.net.bean.UserInfo;
import com.style.net.core2.RetrofitImpl;
import com.style.net.core2.callback.CustomHttpThrowable;
import com.style.net.core2.callback.CustomResourceObserver;
import com.style.net.core2.response.TokenResponse;

import io.reactivex.functions.Consumer;

public class WebServiceActivity extends BaseActivity {

    ActivityWebserviceBinding bd;
    WebServicePresenter presenter;

    @Override
    protected BaseActivityPresenter getPresenter() {
        presenter = new WebServicePresenter(this);

        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_webservice);
        super.setContentView(bd.getRoot());
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public void searchMobile(View v) {
        final String phone = bd.etPhone.getText().toString();
        RetrofitImpl.getInstance().getPhoneInfo(phone).subscribe(new CustomResourceObserver<String>() {
            @Override
            public void onNext(String s) {
                logE(TAG, s);
                bd.tvResult.setText(s);
                //NetworkOnMainThreadException
            }
        });
    }

    public void searchWeather(View v) {
        final String code = bd.etCityCode.getText().toString();
        presenter.searchWeather(code);

    }

    public void responseString(View v) {
        final String code = bd.etCityCode.getText().toString();
        RetrofitImpl.getInstance().getWeather(code).subscribe(new CustomResourceObserver<String>() {
            @Override
            public void onNext(String s) {
                logE(TAG, s);
                bd.tvResult.setText(s);
            }
        });
    }

    public void responseGeneralData(View v) {
        /*RetrofitImpl.getInstance().getKuaiDi("", "").flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String kuaiDis) throws Exception {
                return RetrofitImpl.getInstance().getKuaiDi("", "");
            }
        }).subscribe(new CustomResourceObserver<String>() {
            @Override
            public void onNext(String kuaiDis) {
                logE(TAG, kuaiDis.toString());
                bd.tvResult.setText(kuaiDis.toString());
            }
        });*/
        RetrofitImpl.getInstance().login2("17364814713", "xj19910809").subscribe(new Consumer<UserInfo>() {
            @Override
            public void accept(UserInfo userInfo) throws Exception {
                logE(TAG, userInfo.toString());

            }
        }, new CustomHttpThrowable() {
            @Override
            public void accept(Throwable e) throws Exception {
                super.accept(e);
            }
        });
    }

    public void responseGeneralNoData(View v) {
        RetrofitImpl.getInstance().getToken().subscribe(new Consumer<TokenResponse>() {
            @Override
            public void accept(TokenResponse tokenResponse) throws Exception {
                logE(TAG, tokenResponse.access_token);
                AccountManager.getInstance().setSignKey(tokenResponse.access_token);
            }
        }, new CustomHttpThrowable());
    }

    public void searchWeatherSuccess(String s) {
        logE(TAG, s);
        bd.tvResult.setText(s);
    }
}
