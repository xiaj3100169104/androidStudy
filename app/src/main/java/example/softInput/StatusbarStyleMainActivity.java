package example.softInput;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.dialog.LoadingDialog;
import com.style.dialog.PromptDialog;
import com.style.dialog.SelAvatarDialog;
import com.style.framework.R;
import com.style.framework.databinding.DialogActivityDialogBinding;
import com.style.framework.databinding.StatusbarStyleActivityMainBinding;

/**
 * Created by xiajun on 2016/10/8.
 */
public class StatusbarStyleMainActivity extends BaseActivity {
    StatusbarStyleActivityMainBinding bd;

    public boolean isLightStatusBar() {
        return true;
    }

    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.statusbar_style_activity_main);
        super.setContentView(bd.getRoot());
    }

    @Override
    public void initData() {
        setToolbarTitle("默认状态栏样式，适合白色标题栏的时候");
        bd.setOnItemClickListener(new OnItemClickListener());
    }

    public class OnItemClickListener {

        public void skip1(View v) {
            skip(SoftMode1Activity.class);
        }

        public void skip2(View v) {
            skip(SoftMode2Activity.class);
        }

        public void skip3(View v) {
            skip(SoftMode3Activity.class);
        }

        public void skip4(View v) {
            skip(SoftMode4Activity.class);

        }
    }
}