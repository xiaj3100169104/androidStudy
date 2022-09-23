package example.web_service;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.databinding.ActivityCoroutineBinding;

import org.jetbrains.annotations.Nullable;

public class CoroutineActivity extends BaseTitleBarActivity {

    ActivityCoroutineBinding bd;
    WebServiceViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        bd = ActivityCoroutineBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setTitleBarTitle("coroutine");
        mViewModel = new ViewModelProvider(this).get(WebServiceViewModel.class);
        mViewModel.content.observe(this, s -> setContent(s));
        bd.btnSearchMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMobile();
            }
        });
        bd.btnSearchWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWeather();
            }
        });
    }

    public void searchMobile() {
        final String phone = bd.etPhone.getText().toString();
        mViewModel.getPhoneInfo(phone);
    }

    public void searchWeather() {
        final String code = bd.etCityCode.getText().toString();
        mViewModel.searchWeather(code);
    }

    public void setContent(String s) {
        logE(getTAG(), s.toString());
        bd.tvResult.setText(s.toString());
    }
}
