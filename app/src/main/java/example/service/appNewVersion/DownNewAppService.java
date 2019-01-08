package example.service.appNewVersion;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class DownNewAppService extends IntentService {
    DownNewAppModel mPresenter;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownNewAppService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPresenter = new DownNewAppModel(getApplication());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String versionInfo = intent.getStringExtra("url");
        mPresenter.startDownloadApp(versionInfo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
