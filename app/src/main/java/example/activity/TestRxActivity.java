package example.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.style.base.BaseActivity;
import com.style.framework.R;

import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TestRxActivity extends BaseActivity {

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_test_rx;
        super.onCreate(savedInstanceState);
    }


    @OnClick(R.id.btn_just)
    public void skip1() {
        /*Observable.just("one", "two", "three", "four", "five")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(*//* an Observer *//*);*/
        Flowable.just("hello RxJava 2")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                });
    }

    @OnClick(R.id.btn_just)
    public void skip2() {
        Flowable.just("hello RxJava 2")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s + " -ittianyu";
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                });
    }
}
