package example.address;

import android.media.MediaPlayer;

import com.style.utils.HanyuToPinyin;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.style.base.BaseActivityPresenter;

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
    private MediaPlayer player;

    public AddressPresenter(AddressActivity mActivity) {
        super(mActivity);
    }

    public void getData() {

        Disposable d = Observable.just("").map(param -> {
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
            return list;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> getActivity().setData(list));
        addTask(d);

    }

    public void getRingtone() {
        List<MyRingtone> list = ContactHelper.getRingtone(getActivity());
        player = new MediaPlayer();
        try {
            player.setDataSource(list.get(0).path);
            player.prepareAsync();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.start();

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPlay() {
        if (player != null && player.isPlaying()) {
            player.pause();
            player.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null && player.isPlaying()) {
            player.pause();
            player.stop();
            player.release();
        }
    }
}
