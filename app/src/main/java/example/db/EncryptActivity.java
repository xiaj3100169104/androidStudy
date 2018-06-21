package example.db;

import android.view.View;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.framework.R;
import com.style.framework.databinding.ActivityTemperatureBinding;

public class EncryptActivity extends BaseActivity {

    ActivityTemperatureBinding bd;
    EncryptPresenter presenter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_temperature;
    }

    @Override
    protected BaseActivityPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void initData() {
        bd = getBinding();
        bd.setOnViewClickListener(new OnViewClickListener());
        presenter = new EncryptPresenter(this);
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
