package example.encrypt;

import android.os.Bundle;
import android.view.View;

import com.style.base.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityEncryptBinding;

import org.jetbrains.annotations.Nullable;

public class EncryptActivity extends BaseDefaultTitleBarActivity {

    ActivityEncryptBinding bd;
    EncryptPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_encrypt);
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
