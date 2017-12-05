package example.activity;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.net.core.HttpActionManager;
import com.style.net.core.NetStringCallback;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class WebServiceActivity extends BaseToolBarActivity {
    @Bind(R.id.et_phone)
    AutoCompleteTextView etPhone;
    @Bind(R.id.btn_search)
    Button btnSearch;
    @Bind(R.id.et_city_code)
    AutoCompleteTextView etCityCode;
    @Bind(R.id.btn_search_weather)
    Button btnSearchWeather;
    @Bind(R.id.tv_result)
    TextView tvResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_webservice;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
    }

    @OnClick(R.id.btn_search)
    public void search() {
        final String phone = etPhone.getText().toString();

        HttpActionManager.getInstance().getPhoneInfo(TAG, phone, new NetStringCallback() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                super.onResponse(call, response);
                tvResult.setText(response.body());
            }
        });

    }

    @OnClick(R.id.btn_search_weather)
    public void setBtnSearchWeather() {
        final String code = etCityCode.getText().toString();
        HttpActionManager.getInstance().getWeather(TAG, code, new NetStringCallback(){
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                super.onResponse(call, response);
                tvResult.setText(response.body());
            }
        });

    }
}
