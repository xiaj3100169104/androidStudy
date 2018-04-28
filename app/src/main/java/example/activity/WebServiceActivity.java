package example.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityWebserviceBinding;
import com.style.manager.AccountManager;
import com.style.net.bean.UserInfo;
import com.style.net.core2.RetrofitImpl;
import com.style.net.core2.callback.CustomResourceObserver;
import com.style.net.core2.response.TokenResponse;

public class WebServiceActivity extends BaseToolBarActivity {

    ActivityWebserviceBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_webservice);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {

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
        RetrofitImpl.getInstance().getWeather(code).subscribe(new CustomResourceObserver<String>() {
            @Override
            public void onNext(String s) {
                logE(TAG, s);
                bd.tvResult.setText(s);
            }
        });

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
        RetrofitImpl.getInstance().login2("17364814713", "xj19910809").subscribe(new CustomResourceObserver<UserInfo>() {
            @Override
            public void onNext(UserInfo baseRes) {
                logE(TAG, baseRes.toString());

            }
        });
    }

    public void responseGeneralNoData(View v) {
        RetrofitImpl.getInstance().getToken().subscribe(new CustomResourceObserver<TokenResponse>() {
            @Override
            public void onNext(TokenResponse baseRes) {
                logE(TAG, baseRes.access_token);
                AccountManager.getInstance().setSignKey(baseRes.access_token);
            }
        });
    }
}
