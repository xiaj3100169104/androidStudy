package example.address;

import com.style.utils.HanyuToPinyin;

import java.util.Collections;
import java.util.List;

import example.login.BaseActivityPresenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiajun on 2018/4/29.
 */

public class AddressPresenter extends BaseActivityPresenter<AddressActivity> {
    public AddressPresenter(AddressActivity mActivity) {
        super(mActivity);
    }

    public void getData() {

        Disposable d = Observable.just("").map(new Function<String, List<UploadPhone>>() {
            @Override
            public List<UploadPhone> apply(String activity) throws Exception {
                List<UploadPhone> list = ContactHelper.getContacts(getActivity());
                if (null != list) {
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        String sortLetter = HanyuToPinyin.hanziToCapital(list.get(i).getName());
                        list.get(i).setSortLetters(sortLetter);
                    }
                    // 根据a-z进行排序源数据
                    Collections.sort(list, new UploadPhoneComparator());
                }

                Thread.sleep(2000);
                return list;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<UploadPhone>>() {
                    @Override
                    public void accept(List<UploadPhone> list) throws Exception {
                        getActivity().setData(list);

                    }
                });
        addTask(d);
    }
}
