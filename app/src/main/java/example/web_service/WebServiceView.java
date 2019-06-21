package example.web_service;

import android.arch.lifecycle.LifecycleOwner;

import com.style.base.BaseView;

public interface WebServiceView extends LifecycleOwner {

    void  setPhone(String s);
    void  setWeather(String s);
}
