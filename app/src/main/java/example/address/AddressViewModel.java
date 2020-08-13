package example.address;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import android.media.MediaPlayer;
import androidx.annotation.NonNull;

import com.style.base.BaseViewModel;
import com.style.utils.PinyinUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiajun on 2018/4/29.
 */

public class AddressViewModel extends BaseViewModel {
    MutableLiveData<List<UploadPhone>> contacts = new MutableLiveData<>();
    private MediaPlayer player;

    public AddressViewModel(@NonNull Application application) {
        super(application);
    }

    public void getData() {

        Disposable d = Observable.just("").map(param -> {
            List<UploadPhone> list = ContactHelper.getContacts(getApplication());
            if (null != list) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    String sortLetter = PinyinUtils.getAbbreviation(list.get(i).getName()).substring(0, 1);
                    list.get(i).setSortLetters(sortLetter);
                }
                // 根据a-z进行排序源数据
                Collections.sort(list, new UploadPhoneComparator());
            }
            return list;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> contacts.postValue(list));
        addTask(d);

    }

    public void getRingtone() {
        List<MyRingtone> list = ContactHelper.getRingtone(getApplication());
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
    protected void onCleared() {
        super.onCleared();
        if (player != null && player.isPlaying()) {
            player.pause();
            player.stop();
            player.release();
        }
    }
}
