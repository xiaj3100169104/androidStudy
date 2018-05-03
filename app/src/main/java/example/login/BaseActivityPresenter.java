package example.login;

import com.style.app.AccountManager;
import com.style.net.core2.RetrofitImpl;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by xiajun on 2018/4/29.
 */

public abstract class BaseActivityPresenter<V> {
    private final AccountManager accountManager;
    private final RetrofitImpl httpApi;
    private final CompositeDisposable tasks;
    V mActivity;

    public BaseActivityPresenter(V mActivity) {
        this.mActivity = mActivity;
        accountManager = AccountManager.getInstance();
        httpApi = RetrofitImpl.getInstance();
        tasks = new CompositeDisposable();
    }

    public V getActivity() {
        return mActivity;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public RetrofitImpl getHttpApi() {
        return httpApi;
    }

    public CompositeDisposable getTasks() {
        return tasks;
    }

    protected void addTask(Disposable d) {
        getTasks().add(d);
    }

    public void onDestroy() {
        if (tasks != null)
            tasks.dispose();
        mActivity = null;
    }
}
