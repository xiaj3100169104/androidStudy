package example.encrypt;

import android.view.View;

import com.style.base.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityEncryptBinding;

public class EncryptActivity extends BaseDefaultTitleBarActivity {

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
