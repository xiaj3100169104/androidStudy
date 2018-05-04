package example.webservice;


import com.style.base.BaseActivityPresenter;
import com.style.net.core2.callback.CustomHttpThrowable;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class WebServicePresenter extends BaseActivityPresenter<WebServiceActivity> {

    public WebServicePresenter(WebServiceActivity mActivity) {
        super(mActivity);
    }

    public void searchWeather(String code) {
        Disposable d = getHttpApi().getWeather(code).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                getActivity().showToast("查询天气成功");
                getActivity().searchWeatherSuccess(s);
            }
        },new CustomHttpThrowable(){
            @Override
            public void accept(Throwable e) throws Exception {
                super.accept(e);
                getActivity().showToast("查询天气失败");
            }
        });
        addTask(d);
    }
}
