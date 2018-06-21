package example.db;

import com.style.base.BaseActivityPresenter;
import com.style.bean.User;

/**
 * Created by xiajun on 2018/6/21.
 */

public class EncryptPresenter extends BaseActivityPresenter<EncryptActivity> {
    public EncryptPresenter(EncryptActivity mActivity) {
        super(mActivity);
    }

    public void saveUser() {
        User user = new User("123456789", "zxcvbnm");
        user.setSex("男");
        user.setTelPhone("17364814713");
        user.setUserName("夏军");
        user.setSignKey("osfsnffnuj ekrfasfhaweoirwefnejfwefaslfheoifhefhnewfwfwfpenpnmsnmfnejfic");
        getAccountManager().saveUserEncrypt(user);
    }

    public void getUser() {
        getAccountManager().getUserDecrypt();
    }
}
