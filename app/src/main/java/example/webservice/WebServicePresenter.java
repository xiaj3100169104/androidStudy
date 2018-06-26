package example.webservice;


import android.text.TextUtils;
import android.util.Log;

import com.style.base.BaseActivityPresenter;
import com.style.data.net.bean.UserInfo;
import com.style.data.net.exception.ResultErrorException;
import com.style.data.net.response.TokenResponse;

import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


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
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                handleHttpError(throwable);
                getActivity().showToast("查询天气失败");
            }
        });
        addTask(d);
    }

    public void getPhoneInfo(String phone) {
        Disposable d = getHttpApi().getPhoneInfo(phone).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                getActivity().setPhoneInfo(s);

            }
        });
        addTask(d);
    }

    public void getWeather(String code) {
        Disposable d = getHttpApi().getWeather(code).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                getActivity().setWeatherInfo(s);

            }
        });
        addTask(d);
    }

    public void getKuaiDi(String s, String s1) {
        Disposable d = getHttpApi().getKuaiDi("", "").flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String kuaiDis) throws Exception {
                return getHttpApi().getKuaiDi("", "");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                getActivity().setKuaiDiInfo(s);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                handleHttpError(throwable);
            }
        });
        addTask(d);
    }

    public void login(final String userName, final String pass) {

        Disposable d = getHttpApi().getToken().flatMap(new Function<TokenResponse, ObservableSource<UserInfo>>() {
            @Override
            public ObservableSource<UserInfo> apply(TokenResponse tokenResponse) throws Exception {
                if (TextUtils.isEmpty(tokenResponse.access_token))
                    throw new ResultErrorException(ResultErrorException.REQUEST_FAILED);
                Log.e(TAG, tokenResponse.access_token);
                getAccountManager().setSignKey(tokenResponse.access_token);
                return getHttpApi().login2(userName, pass);
            }
        }).subscribe(new Consumer<UserInfo>() {
            @Override
            public void accept(UserInfo userInfo) throws Exception {
                Log.e(TAG, userInfo.toString());

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                handleHttpError(throwable);
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Log.e(TAG, "sfdfsd");
            }
        });
        addTask(d);
    }
}
