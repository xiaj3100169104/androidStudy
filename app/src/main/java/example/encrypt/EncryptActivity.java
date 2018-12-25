package example.encrypt;

import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.style.base.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityEncryptBinding;

import org.jetbrains.annotations.Nullable;

public class EncryptActivity extends BaseDefaultTitleBarActivity {

    ActivityEncryptBinding bd;
    EncryptViewModel presenter;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_encrypt);
        bd = getBinding();
        bd.setOnViewClickListener(new OnViewClickListener());
        presenter = getViewModel(EncryptViewModel.class);
        String textStr = "本月已成功邀请 <strong><font color=\"#FF0000\">" + 100 + "</font></strong>人";
        bd.tvHtml.setText(Html.fromHtml(textStr));
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
