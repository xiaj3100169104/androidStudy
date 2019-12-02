package example.encrypt;

import android.os.Bundle;
import android.text.Html;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityEncryptBinding;
import com.style.utils.BytesHexStrTranslate;

import org.jetbrains.annotations.Nullable;

public class EncryptActivity extends BaseTitleBarActivity {

    ActivityEncryptBinding bd;
    EncryptViewModel mViewModel;
    private byte[] b22;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_encrypt);
        bd = getBinding();
        mViewModel = getViewModel(EncryptViewModel.class);
        String textStr = "本月已成功邀请 <strong><font color=\"#FF0000\">" + 100 + "</font></strong>人";
        bd.tvHtml.setText(Html.fromHtml(textStr));
        bd.btnAesEncrypt.setOnClickListener(v -> AESEncrypt());
        bd.btnAesDecrypt.setOnClickListener(v -> AESDecrypt());
        bd.btnHexString2Bytes.setOnClickListener(v -> {
            b22 = BytesHexStrTranslate.hexString2Bytes("01");
        });
        bd.btnBytes2HexString.setOnClickListener(v -> {
            String s11 = BytesHexStrTranslate.bytes2HexString(b22);
            bd.tvResult.setText(s11);
        });
    }

    public void AESEncrypt() {
        mViewModel.saveUser();
    }

    public void AESDecrypt() {
        mViewModel.getUser();
    }

}
