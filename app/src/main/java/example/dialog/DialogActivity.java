package example.dialog;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.view.View;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.dialog.LoadingDialog;
import com.style.dialog.MaterialProgressDialog;
import com.style.dialog.PromptDialog;
import com.style.dialog.SelAvatarDialog;
import com.style.framework.R;
import com.style.framework.databinding.DialogActivityDialogBinding;

/**
 * Created by xiajun on 2016/10/8.
 */
public class DialogActivity extends BaseActivity {
    DialogActivityDialogBinding bd;
    private FragmentManager fm;
    private FragmentTransaction bt;
    private MaterialProgressDialog materialDialog;
    private ScaleTestWindow popWindow;

    @Override
    public int getLayoutResId() {
        return R.layout.dialog_activity_dialog;
    }

    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("弹出框");
        fm = getSupportFragmentManager();
        bd.setOnItemClickListener(new OnItemClickListener());

        materialDialog = new MaterialProgressDialog(this);
        materialDialog.show();
        popWindow = new ScaleTestWindow.Builder(this).create();

        bd.ivLogo.setOnClickListener(v -> {
            popWindow.showAsDropDown(v, 0, -v.getHeight());
        });
        bd.btn3.setOnClickListener(v -> {
            showPopupMenu(v);
        });
    }

    private void showPopupMenu(View v) {
        PopupMenu pop = new PopupMenu(this, v);//v是加号控件
        pop.getMenuInflater().inflate(R.menu.user_info, pop.getMenu());
        pop.show();
        pop.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit:
                    break;
                case R.id.report:
                    break;
            }
            return true;
        });

    }

    public class OnItemClickListener {

        public void skip1(View v) {
            PromptDialog promptDialog = new PromptDialog(getContext());
            promptDialog.setTitle("提示");
            promptDialog.setMessage("这是提示信息");
            promptDialog.setListener(new PromptDialog.OnPromptListener() {
                @Override
                public void onPositiveButton() {

                }

                @Override
                public void onNegativeButton() {

                }
            });
            promptDialog.show();
        }

        public void skip2(View v) {
            LoadingDialog loadingDialog = new LoadingDialog(getContext());
            loadingDialog.show();
        }

        public void skip3(View v) {
            SelAvatarDialog selAvatarDialog = new SelAvatarDialog(getContext());
            selAvatarDialog.show();
        }

        public void skip4(View v) {
            /*DialogFragment df= new DialogFragment();
            fm.beginTransaction().add(R.id.layout_root, df , "dialog").commit();
*/
        }
    }
}