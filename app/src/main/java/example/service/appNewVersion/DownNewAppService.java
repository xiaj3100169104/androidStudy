package example.service.appNewVersion;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class DownNewAppService extends IntentService {
    DownNewAppServiceModel mPresenter;

    public DownNewAppService() {
        super("balabalabala");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPresenter = new DownNewAppServiceModel(getApplication());
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
