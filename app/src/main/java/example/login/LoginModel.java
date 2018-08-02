package example.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.style.app.MyApp;
import com.style.base.BaseAndroidViewModel;
import com.style.bean.User;
import com.style.data.net.bean.UserInfo;
import com.style.data.net.response.BaseResult;
import com.style.data.prefs.AccountManager;

import java.net.URL;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;

import example.newwork.response.LoginBean;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by xiajun on 2018/7/13.
 */

public class LoginModel extends BaseAndroidViewModel {

    ObservableBoolean loginSucceed = new ObservableBoolean(false);
    ObservableField<User> user = new ObservableField<>();
    MutableLiveData<Boolean> loginState = new MutableLiveData<>();

    public LoginModel(@NonNull Application application) {
        super(application);
    }

    public void login() {

         /*   new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        getPubkey();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();*/

        User u = new User("sfsf", "fasfgasfg");
        user.set(u);
        u.setPassword("123456");
        //loginSucceed.set(true);
        loginState.setValue(true);
        MyApp app = getApplication();
        app.initRefreshView();
    }

    public void getLoginUser() {
        String a = getPreferences().getCurrentAccount();
    }

    public void login(String userName, String password) {
        User user = new User(userName, password);
        AccountManager.getInstance().setCurrentUser(user);
        synData();
        Disposable d = getHttpApi().login(userName, password).subscribe(new Consumer<BaseResult<LoginBean>>() {
            @Override
            public void accept(BaseResult<LoginBean> loginBeanBaseResult) throws Exception {
                //getActivity().loginSuccess();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //getActivity().loginFailed();
            }
        });
        addTask(d);

    }

    public void synData() {
        /*curUser = AccountManager.getInstance().getCurrentUser();
        List<User> friends = UserDBManager.getInstance().getAllMyFriend(curUser.getUserId());
        if (friends != null && friends.size() > 0) {
        } else {
            for (int i = 2; i < 10; i++) {
                User user = new User(i + "", "123456");
                UserDBManager.getInstance().insertUser(user);
            }

            for (int i = 2; i < 5; i++) {
                Friend bean = new Friend();
                bean.setFriendId(i + "");
                bean.setOwnerId(curUser.getUserId());
                bean.setMark("朋友" + i);
                UserDBManager.getInstance().insertFriend(bean);
            }

        }*/
    }

    public void getPubkey() throws Exception {
        URL url = new URL("https://watch.lemonnc.com");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        //conn.setInstanceFollowRedirects(false);
        conn.connect();
        //initData(conn.getInputStream());
        //Certificate[] certs0 = conn.getLocalCertificates();    //会拿到完整的证书链
        Certificate[] certificates = conn.getServerCertificates();    //会拿到完整的证书链
        X509Certificate cert = (X509Certificate) certificates[0];    //cert[0]是证书链的最下层
        System.out.println("序号：" + cert.getSerialNumber());
        System.out.println("颁发给：" + cert.getSubjectDN().getName());
        System.out.println("颁发者：" + cert.getIssuerDN().getName());
        System.out.println("起始：" + cert.getNotBefore());
        System.out.println("过期：" + cert.getNotAfter());
        System.out.println("算法：" + cert.getSigAlgName());
        System.out.println("公钥：" + cert.getPublicKey().getEncoded());
        System.out.println("指纹：" + getThumbPrint(cert));
        conn.disconnect();
    }

    private static String getThumbPrint(X509Certificate cert) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] der = cert.getEncoded();
        md.update(der);
        byte[] digest = md.digest();
        return bytesToHexString(digest);
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
