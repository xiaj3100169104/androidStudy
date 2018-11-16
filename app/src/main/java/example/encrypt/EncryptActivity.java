package example.encrypt;

import android.view.View;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityEncryptBinding;
import com.style.framework.databinding.ActivityEncrytBinding;

public class EncryptActivity extends BaseTitleBarActivity {

    ActivityEncryptBinding bd;
    EncryptPresenter presenter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_encrypt;
    }

    @Override
    protected void initData() {
        bd = getBinding();
        bd.setOnViewClickListener(new OnViewClickListener());
        presenter = getViewModel(EncryptPresenter.class);
    }

    public class OnViewClickListener {
        public void AESEncrypt(View v) {
            presenter.saveUser();
        }

        public void AESDecrypt(View v) {
            presenter.getUser();
        }
    }
}
