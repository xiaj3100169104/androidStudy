package example.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityWebserviceBinding;
import com.style.net.core.HttpActionManager;
import com.style.net.core.NetStringCallback;

import retrofit2.Call;
import retrofit2.Response;

public class WebServiceActivity extends BaseToolBarActivity {

    ActivityWebserviceBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_webservice);
        initData();
    }

    @Override
    public void initData() {
        super.customTitleOptions(bd.getRoot());

    }

    public void search(View v) {
        final String phone = bd.etPhone.getText().toString();

        HttpActionManager.getInstance().getPhoneInfo(TAG, phone, new NetStringCallback() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                super.onResponse(call, response);
                bd.tvResult.setText(response.body());
            }
        });

    }

    public void searchWeather(View v) {
        final String code = bd.etCityCode.getText().toString();
        HttpActionManager.getInstance().getWeather(TAG, code, new NetStringCallback(){
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                super.onResponse(call, response);
                bd.tvResult.setText(response.body());
            }
        });

    }
}
